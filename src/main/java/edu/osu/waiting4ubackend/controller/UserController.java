package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.DBClient_user;
import edu.osu.waiting4ubackend.entity.User;
import edu.osu.waiting4ubackend.request.UserRegisterRequest;
import edu.osu.waiting4ubackend.response.UserRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import edu.osu.waiting4ubackend.response.UserLoginResponse;

@RestController
public class UserController {
    @CrossOrigin
    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest request) throws IOException {
        User user = new User.UserBuilder()
                .setUserName(request.getUserName())
                .setPassword(request.getPassword())
                .setEmail(request.getEmail())
                .setIntroduction(request.getIntroduction())
                .build();

        DBClient_user dbClient = new DBClient_user();
        //check duplicate userName
        if(dbClient.user_nameExists(user.getUserName())) {
            return new ResponseEntity<>("{\"Error\":  \"The name already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }
        //check duplicate email
        if(dbClient.user_emailExists(user.getEmail())) {
            return new ResponseEntity<>("{\"Error\":  \"The email already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }

        String userId = dbClient.saveUser(user);
        ObjectMapper objectMapper = new ObjectMapper();
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse(userId, request.getUserName(), request.getEmail(), request.getIntroduction());
        return new ResponseEntity<>(objectMapper.writeValueAsString(userRegisterResponse), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<String> login(@PathVariable long id) throws JsonProcessingException {
        DBClient_user dbClient = new DBClient_user();
        User user = dbClient.getUserById(id);
        //check valid user id
        if(user == null) {
            return new ResponseEntity<>("{\"Error\":  \"The user_id doesn't exist\"}", HttpStatus.NOT_FOUND);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        UserLoginResponse userLoginResponse = new UserLoginResponse(user.getId(), user.getUserName(), user.getEmail(), user.getIntroduction(), user.getPreferences());
        return new ResponseEntity<>(objectMapper.writeValueAsString(userLoginResponse), HttpStatus.OK);
    }
}