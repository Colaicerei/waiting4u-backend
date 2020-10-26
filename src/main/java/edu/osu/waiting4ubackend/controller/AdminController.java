package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.AdminDBClient;
import edu.osu.waiting4ubackend.entity.Admin;
import edu.osu.waiting4ubackend.request.AdminLoginRequest;
import edu.osu.waiting4ubackend.request.AdminRegisterRequest;
import edu.osu.waiting4ubackend.response.AdminLoginResponse;
import edu.osu.waiting4ubackend.response.GetAdminResponse;
import edu.osu.waiting4ubackend.response.AdminRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class AdminController {
    //https://www.baeldung.com/spring-controllers#Boot
    //https://howtodoinjava.com/spring5/webmvc/controller-getmapping-postmapping/
    //https://stackoverflow.com/questions/24292373/spring-boot-rest-controller-how-to-return-different-http-status-codes
    @CrossOrigin
    @PostMapping(value = "/admins", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody AdminRegisterRequest request) throws IOException {
        Admin admin = new Admin.AdminBuilder()
                .setUserName(request.getAdminName())
                .setPassword(request.getPassword())
                .setEmail(request.getEmail())
                .build();

        AdminDBClient adminDbClient = new AdminDBClient();
        //check duplicate userName
        if(adminDbClient.userNameExists(admin.getUserName())) {
            return new ResponseEntity<>("{\"Error\":  \"The name already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }
        //check duplicate email
        if(adminDbClient.emailExists(admin.getEmail())) {
            return new ResponseEntity<>("{\"Error\":  \"The email already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }

        String adminId = adminDbClient.saveAdmin(admin);
        //https://www.baeldung.com/jackson-object-mapper-tutorial
        ObjectMapper objectMapper = new ObjectMapper();
        AdminRegisterResponse adminRegisterResponse = new AdminRegisterResponse(adminId, request.getAdminName(), request.getEmail());
        return new ResponseEntity<>(objectMapper.writeValueAsString(adminRegisterResponse), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping(value = "/admins/login", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest adminLoginRequest) throws JsonProcessingException {
        Admin admin = new Admin.AdminBuilder()
                .setEmail(adminLoginRequest.getEmail())
                .setPassword(adminLoginRequest.getPassword())
                .build();

        AdminDBClient adminDbClient = new AdminDBClient();
        //check duplicate existing admin
        String adminId = adminDbClient.adminExists(admin);
        if(adminId == null) {
            return new ResponseEntity<>("{\"Error\":  \"The admin doesn't exist\"}", HttpStatus.UNAUTHORIZED);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            AdminLoginResponse adminLoginResponse = new AdminLoginResponse(adminId);
            return new ResponseEntity<>(objectMapper.writeValueAsString(adminLoginResponse), HttpStatus.OK);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/admins/{id}", produces = "application/json")
    public ResponseEntity<String> getAdmin(@PathVariable long id) throws JsonProcessingException {
        AdminDBClient adminDbClient = new AdminDBClient();
        Admin admin = adminDbClient.getAdminById(id);
        //check valid admin id
        if(admin == null) {
            return new ResponseEntity<>("{\"Error\":  \"Unauthorized user\"}", HttpStatus.NOT_FOUND);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        GetAdminResponse getAdminResponse = new GetAdminResponse(admin.getId(), admin.getUserName(), admin.getEmail(), admin.getPets());
        return new ResponseEntity<>(objectMapper.writeValueAsString(getAdminResponse), HttpStatus.OK);
    }
}
