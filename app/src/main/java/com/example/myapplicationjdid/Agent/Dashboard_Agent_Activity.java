package com.example.myapplicationjdid.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

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
                // Handle the case when the salle data is not found
                System.out.println("No such document!");
            }
        }).addOnFailureListener(e -> {
            // Handle errors
            System.out.println("Error fetching document: " + e.getMessage());
        });
    }

    // Method to decode base64 and open the Excel file
    private void openExcelFile(String base64String) {
        try {
            // Decode Base64 string
            byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
            Log.d("Decoded Bytes", "Length: " + decodedBytes.length);

            // Send the byte array to ViewExcelActivity
            Intent intent = new Intent(Dashboard_Agent_Activity.this, ViewExcelActivity.class);
            intent.putExtra("excelData", decodedBytes);
            startActivity(intent);
        } catch (IllegalArgumentException e) {
            Log.e("Base64 Error", "Failed to decode Base64 string: " + e.getMessage());
        }
    }


    // Add absence function (for now it's empty)
    private void addAbsence() {
        // Get the absence details from the input fields
        String teacherAddress = editTextAbsence.getText().toString().trim();
        TextInputEditText editTextSalle = findViewById(R.id.editTextSalle);
        String salleNumber = editTextSalle.getText().toString().trim();

        // Get today's date
        String currentDate = java.text.DateFormat.getDateInstance().format(new java.util.Date());

        // Validate input
        if (teacherAddress.isEmpty() || salleNumber.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Debugging logs
        Log.d("InputDebug", "Teacher Address: " + teacherAddress);
        Log.d("InputDebug", "Salle Number: " + salleNumber);

        // Create a map to hold the absence data
        Map<String, Object> absenceData = new HashMap<>();
        absenceData.put("teacherAddress", teacherAddress);
        absenceData.put("salleNumber", salleNumber);
        absenceData.put("date", currentDate);

        // Add the absence data to Firestore
        db.collection("absences")
                .add(absenceData)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Absence added successfully with ID: " + documentReference.getId());

                    // After adding absence, send a notification to the teacher
                    sendNotificationToTeacher(teacherAddress);

                    // Clear the input fields
                    editTextAbsence.setText("");
                    editTextSalle.setText("");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error adding absence: " + e.getMessage());
                });
    }
    

    private void sendNotificationToTeacher(String teacherAddress) {
        // Create a Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Set up the URL to send the notification (localhost:3000)
        String serverUrl = "http://10.0.2.2:3000/send-notification"; // Use '10.0.2.2' for Android Emulator (localhost)

        // Prepare the request payload
        JSONObject payload = new JSONObject();
        // You'll need to retrieve the FCM token of the teacher from Firestore
        db.collection("users")
                .whereEqualTo("name", teacherAddress)
                .whereEqualTo("role", "Teacher")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String fcmToken = task.getResult().getDocuments().get(0).getString("fcmToken");

                        if (fcmToken != null) {
                            // Prepare the notification payload
                            try {
                                payload.put("fcmToken", fcmToken);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                payload.put("title", "New Absence Added");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                payload.put("body", "An absence record has been added for you.");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            // Create a POST request with the payload
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverUrl, payload,
                                    response -> {
                                        // Handle success
                                        System.out.println("Notification sent successfully: " + response);
                                    },
                                    error -> {
                                        // Handle error
                                        System.out.println("Error sending notification: " + error.getMessage());
                                    });

                            // Add the request to the request queue
                            requestQueue.add(jsonObjectRequest);
                        } else {
                            System.out.println("FCM Token not found for the teacher.");
                        }
                    }
                });
    }
}
