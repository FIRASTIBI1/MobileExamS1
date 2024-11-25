package com.example.myapplicationjdid.Teacher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplicationjdid.Agent.AbsenceAdapter;
import com.example.myapplicationjdid.R;
import com.example.myapplicationjdid.models.Absence;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private AbsenceAdapter absenceAdapter;
    private List<Absence> absenceList;
    private String teacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Retrieve teacher name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        teacherName = sharedPreferences.getString("userName", null);

        if (teacherName == null || teacherName.isEmpty()) {
            Log.e("DashboardActivity", "No teacher name found in SharedPreferences.");
            return;
        }

        Log.d("DashboardActivity", "Teacher name: " + teacherName);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        absenceList = new ArrayList<>();
        absenceAdapter = new AbsenceAdapter(absenceList);
        recyclerView.setAdapter(absenceAdapter);

        // Load absences for the teacher
        loadAbsences();
    }

    private void loadAbsences() {
        Log.d("DashboardActivity", "Querying absences for teacherAddress: " + teacherName);

        firestore.collection("absences")
                .whereEqualTo("teacherAddress", teacherName) // Match the teacher's name
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    absenceList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Absence absence = document.toObject(Absence.class);
                            absenceList.add(absence);
                            Log.d("DashboardActivity", "Absence added: " + absence.getTeacherAddress());
                        }
                        absenceAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("DashboardActivity", "No absences found for teacherAddress: " + teacherName);
                    }
                })
                .addOnFailureListener(e -> Log.e("DashboardActivity", "Error fetching absences", e));
    }
}
