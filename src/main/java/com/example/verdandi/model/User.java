package com.example.verdandi.model;


public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private int hourlyRate;

    public User() {

    }
    public User(int id,
                String username,
                String password,
                String email,
                int hourlyRate,
                String role) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.hourlyRate = hourlyRate;
    }

    // Getter methods

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


    public int getHourlyRate() {
        return hourlyRate;
    }

    public String getRole() {
        return role;
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
