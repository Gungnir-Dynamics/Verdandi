package com.example.verdandi.model;

public class SubProject {

    private int id;
    private String name;
    private String description;
    private int projectId;

    // CONSTRUCTORS
    public SubProject(int id, String name, String description, int projectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectId = projectId;

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

    public int getProjectId() {return projectId;}

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

    public void setProjectId(int projectId) {this.projectId = projectId;}
}
