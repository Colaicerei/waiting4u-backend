package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.PetDBClient;
import edu.osu.waiting4ubackend.client.PetSearchQueryBuilder;
import edu.osu.waiting4ubackend.client.UserDBClient;
import edu.osu.waiting4ubackend.entity.Pet;
import edu.osu.waiting4ubackend.entity.User;
import edu.osu.waiting4ubackend.request.UserLoginRequest;
import edu.osu.waiting4ubackend.request.UserRegisterRequest;
import edu.osu.waiting4ubackend.response.GetUserResponse;
import edu.osu.waiting4ubackend.response.UserRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import edu.osu.waiting4ubackend.request.UserUpdateRequest;
import edu.osu.waiting4ubackend.response.UserLoginResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import org.springframework.validation.Errors;

@RestController
public class UserController {
    @CrossOrigin
    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest request, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return ControllerHelper.displayErrorMessage(errors);
        }
        User user = new User.UserBuilder()
                .setUserName(request.getUserName())
                .setPassword(request.getPassword())
                .setEmail(request.getEmail())
                .setIntroduction(request.getIntroduction())
                .build();

        UserDBClient userDBClient = new UserDBClient();
        //check duplicate userName
        if (userDBClient.userNameExists(user.getUserName())) {
            return new ResponseEntity<>("{\"Error\":  \"The name already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }
        //check duplicate email
        if (userDBClient.userEmailExists(user.getEmail())) {
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
        if (userId == null) {
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
        if (user == null) {
            return new ResponseEntity<>("{\"Error\":  \"The user doesn't exist\"}", HttpStatus.NOT_FOUND);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        GetUserResponse getUserResponse = new GetUserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getIntroduction());
        return new ResponseEntity<>(objectMapper.writeValueAsString(getUserResponse), HttpStatus.OK);
    }


    @CrossOrigin
    @PatchMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<String> updateUser(@PathVariable long id, @Valid @RequestBody UserUpdateRequest request, Errors errors) throws Exception {
        if (errors.hasErrors()) {
            return ControllerHelper.displayErrorMessage(errors);
        }

        UserDBClient userDBClient = new UserDBClient();
        User user = userDBClient.getUserById(id);

        //check valid user id
        if (user == null) {
            return new ResponseEntity<>("{\"Error\":  \"The user doesn't exist\"}", HttpStatus.NOT_FOUND);
        }

        // formData only send empty string instead of null if no input
        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            if(request.getExistingPassword().equals(user.getPassword())){
                user.setPassword(request.getNewPassword());
            }else{
                return new ResponseEntity<>("{\"Error\":  \"Unauthorized operation: existing password does not match our record\"}", HttpStatus.FORBIDDEN);
            }
        }

        if (!request.getIntroduction().equals(user.getIntroduction())) {
            user.setIntroduction(request.getIntroduction());
        }

        String userId = userDBClient.updateUser(user, id);
        ObjectMapper objectMapper = new ObjectMapper();
        GetUserResponse getUserResponse = new GetUserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getIntroduction());
        return new ResponseEntity<>(objectMapper.writeValueAsString(getUserResponse), HttpStatus.OK);
    }
}
