package com.example.myapplicationjdid.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationjdid.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        // Convert base64 to byte array
        byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);

        // Here you would implement logic to view the Excel file using a library like Apache POI or another method
        // For example, you might save the file temporarily or use an Excel viewer

        // Example: Save the Excel file temporarily and open with a viewer or read its contents
        Intent intent = new Intent(Dashboard_Agent_Activity.this, ViewExcelActivity.class);
        intent.putExtra("excelData", decodedBytes);
        startActivity(intent);
    }

    // Add absence function (for now it's empty)
    private void addAbsence() {
        // Get the absence details from the first input field
        String teacherAddress = editTextAbsence.getText().toString().trim();

        // Get the salle number from the second input field
        TextInputEditText editTextSalle = findViewById(R.id.editTextSalle);
        String salleNumber = editTextSalle.getText().toString().trim();

        // Get today's date
        String currentDate = java.text.DateFormat.getDateInstance().format(new java.util.Date());

        // Validate input
        if (teacherAddress.isEmpty() || salleNumber.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Create a map to hold the data
        Map<String, Object> absenceData = new HashMap<>();
        absenceData.put("teacherAddress", teacherAddress);
        absenceData.put("salleNumber", salleNumber);
        absenceData.put("date", currentDate);

        // Add the data to Firestore
        db.collection("absences")
                .add(absenceData)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Absence added successfully with ID: " + documentReference.getId());
                    // Clear the input fields
                    editTextAbsence.setText("");
                    editTextSalle.setText("");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error adding absence: " + e.getMessage());
                });
    }

}
