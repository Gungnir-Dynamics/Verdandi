package com.example.verdandi.model;

public class SubProject {

    private int id;
    private String name;
    private String description;

    // CONSTRUCTORS
    public SubProject(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public SubProject(){

    }
// GETTERS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
}
