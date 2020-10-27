package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLoginResponse {
    @JsonProperty("user_id")
    private String id;

    public UserLoginResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
