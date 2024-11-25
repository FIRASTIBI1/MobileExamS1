package com.example.myapplicationjdid.models;

public class Absence {
    private String teacherAddress;
    private String salleNumber;
    private String date;

    // Default constructor for Firestore
    public Absence() {}

    // Constructor with parameters
    public Absence(String teacherAddress, String salleNumber, String date) {
        this.teacherAddress = teacherAddress;
        this.salleNumber = salleNumber;
        this.date = date;
    }

    // Getters
    public String getTeacherAddress() {
        return teacherAddress;
    }

    public String getSalleNumber() {
        return salleNumber;
    }

    public String getDate() {
        return date;
    }
}
