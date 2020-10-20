package edu.osu.waiting4ubackend.entity;

import java.util.List;

/**
 * Entity for User class
 */
public class User {
    private String id;
    private String userName;
    private String password;
    private String email;
    private List<String> preferences;

    public User(String id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPreferences() {
        return preferences;
    }
}
