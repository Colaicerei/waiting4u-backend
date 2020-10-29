package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetUserResponse {
    @JsonProperty("user_id")
    private String id;
    @JsonProperty("user_name")
    private String userName;
    private String email;
    private String introduction;
    private List<String> preferences;

    public GetUserResponse(String id, String userName, String email, String introduction, List<String> preferences) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.introduction = introduction;
        this.preferences = preferences;
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

    public String getIntroduction(){ return introduction; }

    public List<String> getPreferences() {
        return preferences;
    }
}
