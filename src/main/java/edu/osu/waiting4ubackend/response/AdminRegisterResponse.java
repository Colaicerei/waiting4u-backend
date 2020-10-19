package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminRegisterResponse {
    @JsonProperty("admin_id")
    private String id;
    @JsonProperty("admin_name")
    private String adminName;
    private String email;

    public AdminRegisterResponse(String id, String adminName, String email) {
        this.id = id;
        this.adminName = adminName;
        this.email = email;
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

}
