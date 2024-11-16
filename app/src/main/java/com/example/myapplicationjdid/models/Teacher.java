package com.example.myapplicationjdid.models;

import java.io.Serializable;

public class Teacher implements Serializable {
    private int id;
    private String nom;
    private String prenom;
    private String matiere;

    // Constructeur par défaut
    public Teacher() {}

    // Constructeur avec paramètres
    public Teacher(int id, String nom, String prenom, String matiere) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matiere = matiere;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    // Méthode toString (utile pour le débogage ou l'affichage)
    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", matiere='" + matiere + '\'' +
                '}';
    }
}
