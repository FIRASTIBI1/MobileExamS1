package com.example.myapplicationjdid.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplicationjdid.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard_Agent_Activity extends AppCompatActivity {

    private Spinner spinnerSalles;
    private Button buttonSeeSchedule, buttonAddAbsence;
    private TextInputEditText editTextAbsence;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_agent);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        spinnerSalles = findViewById(R.id.spinnerSalles);
        buttonSeeSchedule = findViewById(R.id.buttonSeeSchedule);
        editTextAbsence = findViewById(R.id.editTextAbsence);
        buttonAddAbsence = findViewById(R.id.buttonAddAbsence);

        // List of 10 salles (rooms)
        String[] salles = {
                "Salle 1", "Salle 2", "Salle 3", "Salle 4", "Salle 5",
                "Salle 6", "Salle 7", "Salle 8", "Salle 9", "Salle 10"
        };

        // Set up the spinner with the salle names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, salles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSalles.setAdapter(adapter);

        // Set an item selected listener for the spinner
        spinnerSalles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Enable the button when a salle is selected
                buttonSeeSchedule.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Disable the button if no salle is selected
                buttonSeeSchedule.setEnabled(false);
            }
        });

        // Set the click listener for the button to fetch emploi
        buttonSeeSchedule.setOnClickListener(v -> fetchEmploi());

        // Set the click listener for the Add Absence button
        buttonAddAbsence.setOnClickListener(v -> addAbsence());
    }

    // Fetch the emploi for the selected salle and display the schedule
    private void fetchEmploi() {
        String selectedSalle = spinnerSalles.getSelectedItem().toString();

        // Fetch the base64 encoded Excel file from Firestore
        DocumentReference docRef = db.collection("emploi").document(selectedSalle);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String base64String = documentSnapshot.getString("excelFileBase64");

                // If base64 string is found, open the Excel file
                if (base64String != null) {
                    openExcelFile(base64String);
                }
            } else {
                Log.e("FetchEmploi", "No document found for salle: " + selectedSalle);
            }
        }).addOnFailureListener(e -> {
            Log.e("FetchEmploi", "Error fetching document: " + e.getMessage());
        });
    }

    // Method to decode base64 and open the Excel file
    private void openExcelFile(String base64String) {
        try {
            // Decode Base64 string
            byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
            Log.d("DecodedBytes", "Length: " + decodedBytes.length);

            // Send the byte array to ViewExcelActivity
            Intent intent = new Intent(Dashboard_Agent_Activity.this, ViewExcelActivity.class);
            intent.putExtra("excelData", decodedBytes);
            startActivity(intent);
        } catch (IllegalArgumentException e) {
            Log.e("Base64Error", "Failed to decode Base64 string: " + e.getMessage());
        }
    }

    // Add absence function
    private void addAbsence() {
        String teacherAddress = editTextAbsence.getText().toString().trim();
        TextInputEditText editTextSalle = findViewById(R.id.editTextSalle);
        String salleNumber = editTextSalle.getText().toString().trim();

        // Get today's date
        String currentDate = java.text.DateFormat.getDateInstance().format(new java.util.Date());

        // Validate input
        if (teacherAddress.isEmpty() || salleNumber.isEmpty()) {
            Log.e("AddAbsence", "Please fill in all fields.");
            return;
        }

        Map<String, Object> absenceData = new HashMap<>();
        absenceData.put("teacherAddress", teacherAddress);
        absenceData.put("salleNumber", salleNumber);
        absenceData.put("date", currentDate);

        db.collection("absences")
                .add(absenceData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("AddAbsence", "Absence added successfully with ID: " + documentReference.getId());
                    sendNotificationToTeacher(teacherAddress);
                    editTextAbsence.setText("");
                    editTextSalle.setText("");
                })
                .addOnFailureListener(e -> Log.e("AddAbsence", "Error adding absence: " + e.getMessage()));
    }

    private void sendNotificationToTeacher(String teacherAddress) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String serverUrl = "http://10.0.2.2:3000/send-notification";

        JSONObject payload = new JSONObject();
        db.collection("users")
                .whereEqualTo("name", teacherAddress)
                .whereEqualTo("role", "Teacher")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String fcmToken = task.getResult().getDocuments().get(0).getString("fcmToken");

                        if (fcmToken != null) {
                            try {
                                payload.put("fcmToken", fcmToken);
                                payload.put("title", "New Absence Added");
                                payload.put("body", "An absence record has been added for you.");
                            } catch (JSONException e) {
                                Log.e("NotificationError", "Failed to create JSON payload: " + e.getMessage());
                                return;
                            }

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverUrl, payload,
                                    response -> Log.d("NotificationSuccess", "Notification sent successfully: " + response),
                                    error -> Log.e("NotificationError", "Error sending notification: " + error.getMessage()));

                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                    5000,
                                    3,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            ));

                            requestQueue.add(jsonObjectRequest);
                        } else {
                            Log.e("NotificationError", "FCM Token not found for teacher: " + teacherAddress);
                        }
                    } else {
                        Log.e("NotificationError", "Failed to fetch teacher's FCM token. Task success: " + task.isSuccessful());
                        if (task.getException() != null) {
                            Log.e("NotificationError", "Firestore exception: " + task.getException().getMessage());
                        }
                    }
                });
    }
}
