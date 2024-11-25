package com.example.myapplicationjdid.Teacher;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationjdid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ClaimsActivity extends AppCompatActivity {

    private FirebaseFirestore firestore; // Instance de Firestore
    private FirebaseAuth firebaseAuth;  // Instance de FirebaseAuth
    private String teacherName;         // Nom de l'enseignant connecté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);

        // Initialiser Firebase
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Récupérer l'utilisateur connecté
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            teacherName = currentUser.getDisplayName(); // Nom de l'utilisateur
            if (teacherName == null || teacherName.isEmpty()) {
                teacherName = currentUser.getEmail(); // Utiliser l'email si le nom est indisponible
            }
        } else {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité si aucun utilisateur n'est connecté
            return;
        }

        EditText claimMessage = findViewById(R.id.editTextClaimMessage);
        Button submitClaimButton = findViewById(R.id.buttonSubmitClaim);

        submitClaimButton.setOnClickListener(v -> {
            String message = claimMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                // Ajouter la réclamation à Firestore
                addClaimToFirestore(teacherName, message);

                claimMessage.setText(""); // Clear the input field
            } else {
                Toast.makeText(this, "Veuillez saisir une réclamation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addClaimToFirestore(String teacherName, String message) {
        // Préparer les données à ajouter dans Firestore
        Map<String, Object> claim = new HashMap<>();
        claim.put("teacherEmail", teacherName);
        claim.put("claimContent", message);
        claim.put("timestamp", System.currentTimeMillis()); // Ajout d'un timestamp

        // Ajouter dans la collection "claims"
        firestore.collection("claims")
                .add(claim)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Réclamation envoyée avec succès", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de l'envoi de la réclamation", Toast.LENGTH_SHORT).show();
                });
    }
}
