package com.example.myapplicationjdid.Agent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationjdid.R;
import com.example.myapplicationjdid.models.Absence;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AbsenceActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AbsenceAdapter adapter;
    private List<Absence> absenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_absence2);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        absenceList = new ArrayList<>();
        adapter = new AbsenceAdapter(absenceList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        fetchAbsences();
    }

    private void fetchAbsences() {
        db.collection("absences")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Absence> absences = queryDocumentSnapshots.toObjects(Absence.class);
                    absenceList.clear();
                    absenceList.addAll(absences);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error fetching data: " + e.getMessage());
                });
    }
}
