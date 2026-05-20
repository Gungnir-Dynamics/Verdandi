package com.example.verdandi.model;

public class Role {
    private int id;
    private String roleName;

    public Role() {}

    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }


    // GETTERS

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    //SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
