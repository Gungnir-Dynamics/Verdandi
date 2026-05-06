package com.example.verdandi.model;


import java.time.LocalDate;
import java.util.Date;

public class Project {

    private int id;
    private String name;
    private String description;
    private int time;
    private LocalDate deadline;
    private LocalDate creationDate;

// CONSTRUCTORS
    public Project(LocalDate creationDate, LocalDate deadline,
                   int time, String description, String name, int id) {
        this.creationDate = creationDate;
        this.deadline = deadline;
        this.time = time;
        this.description = description;
        this.name = name;
        this.id = id;
    }

    public Project(){

    }


    // GETTERS
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public int getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

// SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
