package edu.osu.waiting4ubackend.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity for User class
 */
public class User {
    private String id;
    private String userName;
    private String password;
    private String email;
    private String introduction;
    private List<String> preferences;

    public User(){
    }

    /*public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.preferences = new ArrayList<>();
        this.introduction = "";
    }*/

    public User(String userName, String password, String email, String introduction) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.preferences = new ArrayList<>();
        this.introduction = introduction;
    }

    public String getId() {
        return id;
    }

    //public void setId(String id){ this.id = id; }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String newPassword) { this.password = newPassword; }

    public List<String> getPreferences() {
        return preferences;
    }

    public String getIntroduction() { return introduction; }

    public void setIntroduction(String newIntroduction) { this.introduction = newIntroduction; }

    public void addPreference(String petType){
        preferences.add(petType);
    }
}
