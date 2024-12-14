package com.example.myapplicationjdid;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class statistiques extends AppCompatActivity {

    private static final String TAG = "statistiques";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        // Référence aux graphiques
        BarChart barChart = findViewById(R.id.barChart);
        PieChart pieChart = findViewById(R.id.pieChart);

        // Charger les données Firebase et configurer les graphiques
        loadFirebaseData(barChart, pieChart);
    }

    private void loadFirebaseData(BarChart barChart, PieChart pieChart) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference absencesRef = db.collection("absences");

        // Récupérer les données des absences
        absencesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Integer> absenceCountPerTeacher = new HashMap<>();

                // Parcourir les documents de la collection
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String teacher = document.getString("teacherAddress");
                    if (teacher != null) {
                        int currentCount = absenceCountPerTeacher.getOrDefault(teacher, 0);
                        absenceCountPerTeacher.put(teacher, currentCount + 1);
                    }
                }

                // Préparer les données pour les graphiques
                setupBarChart(barChart, absenceCountPerTeacher);
                setupPieChart(pieChart, absenceCountPerTeacher);
            } else {
                Log.e(TAG, "Erreur lors de la récupération des données Firebase", task.getException());
            }
        });
    }

    private void setupBarChart(BarChart barChart, Map<String, Integer> data) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> teacherNames = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            barEntries.add(new BarEntry(index, entry.getValue()));
            teacherNames.add(entry.getKey());
            index++;
        }

        // Mise à jour de la couleur (anciennement Color.MAGENTA)
        BarDataSet barDataSet = new BarDataSet(barEntries, "Nombre d'absences");
        barDataSet.setColor(Color.parseColor("#E1FFBB"));

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if ((int) value >= 0 && (int) value < teacherNames.size()) {
                    return teacherNames.get((int) value);
                } else {
                    return ""; // Valeur par défaut si l'indice est hors limites
                }
            }
        });
        barChart.invalidate(); // Actualiser le graphique
    }

    private void setupPieChart(PieChart pieChart, Map<String, Integer> data) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Absences par enseignant");

        // Mise à jour des couleurs (remplacement de RED, BLUE, GREEN, YELLOW)
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#F29F58"));
        colors.add(Color.parseColor("#AB4459"));
        colors.add(Color.parseColor("#441752"));

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Actualiser le graphique
    }
}
