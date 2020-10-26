package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetAdminResponse {
    @JsonProperty("admin_id")
    private String id;
    @JsonProperty("admin_name")
    private String adminName;
    private String email;
    private List<String> pets;

    public GetAdminResponse(String id, String adminName, String email, List<String> pets) {
        this.id = id;
        this.adminName = adminName;
        this.email = email;
        this.pets = pets;
    }

    public String getId() {
        return id;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPets() {
        return pets;
    }
}
