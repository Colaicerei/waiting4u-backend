package edu.osu.waiting4ubackend.request;

public class UserLoginRequest {
    private String password;
    private String email;

    public UserLoginRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
