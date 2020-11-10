package edu.osu.waiting4ubackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AdminUpdateRequest {
    // not sure why there will be error creating instance if only has one member of password
    @JsonProperty("admin_name")
    @Size(min = 3, max = 12, message = "Name should be 3 to 12 characters")
    @Pattern(regexp = "^\\w+(\\d+)?", message = "Name must letters and numbers only")
    private String adminName;
    //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    @JsonProperty("new_password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
        message = "Password should be 8 to 16 characters and contains at least one uppercase, one lowercase and one number")
    private String password;

    public AdminUpdateRequest(String adminName, String password) {
        this.adminName = adminName;
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getPassword() {
        return password;
    }

}
