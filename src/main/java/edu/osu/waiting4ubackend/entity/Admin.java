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
    private List<String> pets;

    public Admin() {
    }

    private Admin(AdminBuilder adminBuilder) {
        this.id = adminBuilder.id;
        this.userName = adminBuilder.userName;
        this.password = adminBuilder.password;
        this.email = adminBuilder.email;
        this.pets = adminBuilder.pets;
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

    public List<String> getPets() {

        return pets;
    }

    public void addPet(String petId) {
        pets.add(petId);
    }

    public static class AdminBuilder {
        private String id;
        private String userName;
        private String password;
        private String email;
        private List<String> pets = new ArrayList<>();

        public AdminBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public AdminBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public AdminBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public AdminBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public AdminBuilder setPets(List<String> pets) {
            this.pets = pets;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
 }
