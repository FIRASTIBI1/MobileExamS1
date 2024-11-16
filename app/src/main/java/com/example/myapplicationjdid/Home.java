package com.example.myapplicationjdid;

import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the button
        MaterialButton addAbsenceButton = findViewById(R.id.addAbsenceButton);

        // Set the button's text color and icon tint programmatically (if needed)
        addAbsenceButton.setTextColor(ContextCompat.getColor(this, R.color.customColor));
        addAbsenceButton.setIconTint(ContextCompat.getColorStateList(this, R.color.customColor));
        addAbsenceButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.customColor));
    }
}
