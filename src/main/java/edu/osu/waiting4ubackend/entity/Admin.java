package edu.osu.waiting4ubackend.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity for Admin class
 */
public class Admin {
    private String id;
    private String userName;
    private String password;
    private String email;
    private List<Integer> pets;

    public Admin() {
    }

    public Admin(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.pets = new ArrayList<>();
    }

    public Admin(String id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.pets = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Integer> getPets() {
        return pets;
    }

    public void addPet(int petId) {
        pets.add(petId);
    }
 }
