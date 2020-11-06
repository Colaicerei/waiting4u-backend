package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.UserDBClient;
import edu.osu.waiting4ubackend.entity.User;
import edu.osu.waiting4ubackend.request.UserLoginRequest;
import edu.osu.waiting4ubackend.request.UserRegisterRequest;
import edu.osu.waiting4ubackend.response.GetUserResponse;
import edu.osu.waiting4ubackend.response.UserRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.osu.waiting4ubackend.request.UserUpdateRequest;

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

        UserDBClient userDBClient = new UserDBClient();
        //check duplicate userName
        if(userDBClient.userNameExists(user.getUserName())) {
            return new ResponseEntity<>("{\"Error\":  \"The name already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }
        //check duplicate email
        if(userDBClient.userEmailExists(user.getEmail())) {
            return new ResponseEntity<>("{\"Error\":  \"The email already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }

        String userId = userDBClient.saveUser(user);
        ObjectMapper objectMapper = new ObjectMapper();
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse(userId, request.getUserName(), request.getEmail(), request.getIntroduction());
        return new ResponseEntity<>(objectMapper.writeValueAsString(userRegisterResponse), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping(value = "/users/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest userLoginRequest) throws JsonProcessingException {
        User user = new User.UserBuilder()
                .setEmail(userLoginRequest.getEmail())
                .setPassword(userLoginRequest.getPassword())
                .build();

        UserDBClient userDbClient = new UserDBClient();
        //get id if user exists
        String userId = userDbClient.userExists(user);
        if(userId == null) {
            return new ResponseEntity<>("{\"Error\":  \"Email or password do not match our records\"}", HttpStatus.UNAUTHORIZED);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            UserLoginResponse userLoginResponse = new UserLoginResponse(userId);
            return new ResponseEntity<>(objectMapper.writeValueAsString(userLoginResponse), HttpStatus.OK);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<String> getUser(@PathVariable long id) throws JsonProcessingException {
        UserDBClient userDBClient = new UserDBClient();
        User user = userDBClient.getUserById(id);
        //check valid user id
        if(user == null) {
            return new ResponseEntity<>("{\"Error\":  \"The user doesn't exist\"}", HttpStatus.NOT_FOUND);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        //GetUserResponse getUserResponse = new GetUserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getIntroduction(), user.getPreferences());
        return new ResponseEntity<>(objectMapper.writeValueAsString(user), HttpStatus.OK);
    }


    @CrossOrigin
    @PatchMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<String> updateUser(@Valid @PathVariable long id, @Valid @RequestBody UserUpdateRequest request) throws IOException{
        UserDBClient userDBClient = new UserDBClient();
        User user = userDBClient.getUserById(id);

        //check valid user id
        if(user == null) {
            return new ResponseEntity<>("{\"Error\":  \"The user doesn't exist\"}", HttpStatus.NOT_FOUND);
        }

        // adding preferences will be handled by its own "apply/add" button
        /*if(request.getNewPreference() != null){
            user.addPreference(request.getNewPreference());
            userDBClient.updatePreferences(request.getNewPreference(), id);
        }

        if(request.getUserName() != null){
            //check duplicate userName
           if(!request.getUserName().equals(user.getUserName()) && userDBClient.userNameExists(request.getUserName())) {
                return new ResponseEntity<>("{\"Error\":  \"The name already exists, please use another one\"}", HttpStatus.FORBIDDEN);
            }
            user.setUserName(request.getUserName());
        }*/

        // formData only send empty string instead of null if no input
        if(request.getPassword() != ""){
            user.setPassword(request.getPassword());
        }

        if(!request.getIntroduction().equals(user.getIntroduction())){
            user.setIntroduction(request.getIntroduction());
        }

        String userId = userDBClient.updateUser(user, id);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(user), HttpStatus.OK);
    }
}
