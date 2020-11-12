package edu.osu.waiting4ubackend.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Pattern;

public class UserUpdateRequest {
    @JsonProperty("new_password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
            message = "Password should be 8 to 16 characters and contains at least one uppercase, one lowercase and one number")
    private String newPassword;
    private String introduction;
    @JsonProperty("existing_password")
    private String existingPassword;
    //private String newPreference;

    public UserUpdateRequest(String existingPassword, String newPassword, String introduction) {
        this.existingPassword = existingPassword;
        this.newPassword = newPassword;
        this.introduction = introduction;
    }

    public String getExistingPassword() {
        return existingPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getIntroduction() {
        return introduction;
    }

}
