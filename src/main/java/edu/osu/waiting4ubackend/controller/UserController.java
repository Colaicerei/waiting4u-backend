package edu.osu.waiting4ubackend.controller;

import edu.osu.waiting4ubackend.response.UserRegisterResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.DBClient;
import edu.osu.waiting4ubackend.entity.User;
import edu.osu.waiting4ubackend.request.UserRegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {
    @CrossOrigin
    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest request) throws IOException, ExecutionException, InterruptedException {
        User user = new User(request.getUserName(), request.getPassword(), request.getEmail(), request.getIntroduction());
        DBClient dbClient = new DBClient();
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
}