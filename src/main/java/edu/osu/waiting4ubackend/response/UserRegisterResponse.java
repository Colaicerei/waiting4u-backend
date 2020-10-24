package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegisterResponse {
    @JsonProperty("user_id")
    private String id;
    @JsonProperty("user_name")
    private String userName;
    private String email;
    private String introduction;

    public UserRegisterResponse(String id, String userName, String email, String introduction) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.introduction = introduction;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getIntroduction() { return introduction; }

}
