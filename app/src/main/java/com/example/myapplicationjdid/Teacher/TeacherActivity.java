package com.example.myapplicationjdid.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationjdid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher2);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set the listener for menu item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId(); // Get the selected item ID

            if (id == R.id.nav_dashboard) {
                openDashboard();
                return true;

            } else if (id == R.id.nav_notifications) {
                openNotifications();
                return true;

            } else if (id == R.id.nav_absences) {
                openAbsences();
                return true;

            } else if (id == R.id.nav_claims) {
                openClaims();
                return true;
            }

            return false; // Return false if no item is selected
        });
    }

    private void openDashboard() {
        Intent dashboardIntent = new Intent(TeacherActivity.this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }

    private void openNotifications() {
        Intent notificationsIntent = new Intent(TeacherActivity.this, NotificationsActivity.class);
        startActivity(notificationsIntent);
    }

    private void openAbsences() {
        Intent absencesIntent = new Intent(TeacherActivity.this, AbsencesActivity.class);
        startActivity(absencesIntent);
    }

    private void openClaims() {
        Intent claimsIntent = new Intent(TeacherActivity.this, ClaimsActivity.class);
        startActivity(claimsIntent);
    }
}
