package edu.osu.waiting4ubackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;
import java.util.List;

public class UserUpdateRequest {
    @JsonProperty("new_password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
            message = "Password should be 8 to 16 characters and contains at least one uppercase, one lowercase and one number")
    private String password;
    private String introduction;
    //private String newPreference;

    public UserUpdateRequest(String password, String introduction) {
        this.password = password;
        this.introduction = introduction;
    }

    public String getPassword() {
        return password;
    }

    public String getIntroduction() {
        return introduction;
    }

}
