package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminLoginResponse {
    @JsonProperty("admin_id")
    private String id;

    public AdminLoginResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
