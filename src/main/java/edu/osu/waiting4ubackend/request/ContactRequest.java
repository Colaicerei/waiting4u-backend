package edu.osu.waiting4ubackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ContactRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    private String petName;
    @NotEmpty
    private String message;

    // missing introduction will be handled at front end by replacing with empty string
    public ContactRequest(String name, String petName, String email, String message) {
        this.name = name;
        this.petName = petName;
        this.email = email;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPetName() {
        return petName;
    }

    public String getMessage() { return message; }

}
