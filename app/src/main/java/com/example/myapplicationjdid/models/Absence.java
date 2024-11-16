package com.example.myapplicationjdid.models;

import java.io.Serializable;
import java.util.Date;

public class Absence implements Serializable {
    private int id;
    private int teacherId;  // ID de l'enseignant associé à cette absence
    private Date date;
    private String motif;

    // Constructeur par défaut
    public Absence() {}

    // Constructeur avec paramètres
    public Absence(int id, int teacherId, Date date, String motif) {
        this.id = id;
        this.teacherId = teacherId;
        this.date = date;
        this.motif = motif;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    // Méthode toString (utile pour le débogage ou affichage)
    @Override
    public String toString() {
        return "Absence{" +
                "id=" + id +
                ", teacherId=" + teacherId +
                ", date=" + date +
                ", motif='" + motif + '\'' +
                '}';
    }
}
