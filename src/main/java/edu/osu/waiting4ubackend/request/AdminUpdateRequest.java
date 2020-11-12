package edu.osu.waiting4ubackend.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Pattern;

public class AdminUpdateRequest {
    @JsonProperty("new_password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
        message = "Password should be 8 to 16 characters and contains at least one uppercase, one lowercase and one number")
    private String newPassword;
    @JsonProperty("existing_password")
    private String existingPassword;

    public AdminUpdateRequest(String existingPassword, String newPassword) {
        this.existingPassword = existingPassword;
        this.newPassword = newPassword;
    }

    public String getExistingPassword() {
        return existingPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
