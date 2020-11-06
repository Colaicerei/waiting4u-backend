package edu.osu.waiting4ubackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UserUpdateRequest {
    //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
    message = "Password should be 8 to 16 characters and contains at least one uppercase, one lowercase and one number")
    @JsonProperty("new_password")
    private String password;
    @JsonProperty("new_introduction")
    private String introduction;
    //private String newPreference;

    public UserUpdateRequest(String password, String introduction) {
        this.password = password;
        this.introduction = introduction;
    }

    public String getPassword() {
        return password;
    }

    public String getIntroduction() { return introduction; }

}
