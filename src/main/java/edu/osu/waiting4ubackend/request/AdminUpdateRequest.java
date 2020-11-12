package edu.osu.waiting4ubackend.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Pattern;

public class AdminUpdateRequest {
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
        message = "Password should be 8 to 16 characters and contains at least one uppercase, one lowercase and one number")
    private String new_password;
    private String existing_password;

    public AdminUpdateRequest(String existing_password, String new_password) {
        this.existing_password = existing_password;
        this.new_password = new_password;
    }

    public String getExistingPassword() {
        return existing_password;
    }

    public String getNewPassword() {
        return new_password;
    }

}
