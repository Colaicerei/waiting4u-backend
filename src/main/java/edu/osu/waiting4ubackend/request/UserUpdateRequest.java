package edu.osu.waiting4ubackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UserUpdateRequest {
    //https://stackoverflow.com/questions/8560348/different-names-of-json-property-during-serialization-and-deserialization
    @JsonProperty("user_name")
    //https://phoenixnap.com/kb/spring-boot-validation-for-rest-services
    @Size(min = 3, max = 12, message = "Name should be 3 to 12 characters")
    //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    @Pattern(regexp = "^\\w+(\\d+)?", message = "Name must be letters and numbers only")
    private String userName;
    //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
    //    message = "Password should be 8 to 16 characters and contains at least one uppercase, one lowercase and one number")
    private String password;
    //private String email;
    private String introduction;

    public UserUpdateRequest(String userName, String password, String introduction) {
        this.userName = userName;
        this.password = password;
        this.introduction = introduction;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getIntroduction() { return introduction; }
}
