package com.example.verdandi.model;

public class Assignment {

    private int profileId;
    private int projectId;

    public Assignment() {
    }

    public Assignment(int profileId, int projectId) {
        this.profileId = profileId;
        this.projectId = projectId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}