package com.example.myapplicationjdid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.myapplicationjdid.models.User;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;  // Firebase Auth instance
    private FirebaseFirestore db;  // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Login", "onCreate called");  // Log when the activity is created

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find the views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Set up the button click listener for login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Log the inputs
                Log.d("Login", "Email: " + email);
                Log.d("Login", "Password: " + password);

                // Validate the inputs
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign in the user
                signInUser(email, password);
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Fetch user details from Firestore
                            fetchUserDetails(user.getUid());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Error: ", task.getException());
                    }
                });
    }

    private void fetchUserDetails(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Map Firestore document to User model
                            User user = document.toObject(User.class);
                            if (user != null) {
                                user.setId(userId); // Add Firebase UID to the user model
                                Log.d("Login", "User fetched: " + user);

                                // Redirect user based on role
                                redirectToRoleActivity(user.getRole());
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to parse user data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "User not found in database", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Error fetching user data: ", task.getException());
                    }
                });
    }

    private void redirectToRoleActivity(String role) {
        Intent intent;
        if ("Admin".equalsIgnoreCase(role)) {
            intent = new Intent(MainActivity.this, AdminActivity.class);
        } else if ("Teacher".equalsIgnoreCase(role)) {
            intent = new Intent(MainActivity.this, TeacherActivity.class);
        } else if ("Agent".equalsIgnoreCase(role)) {
            intent = new Intent(MainActivity.this, AgentActivity.class);
        } else {
            Toast.makeText(MainActivity.this, "Invalid role", Toast.LENGTH_SHORT).show();
            return;  // Stop further execution
        }

        startActivity(intent);
        finish();  // Close login activity
    }
}
