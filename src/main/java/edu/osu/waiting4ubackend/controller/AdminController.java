package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.DBClient;
import edu.osu.waiting4ubackend.entity.Admin;
import edu.osu.waiting4ubackend.request.AdminRegisterRequest;
import edu.osu.waiting4ubackend.response.AdminLoginResponse;
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

        DBClient dbClient = new DBClient();
        //check duplicate userName
        if(dbClient.userNameExists(admin.getUserName())) {
            return new ResponseEntity<>("{\"Error\":  \"The name already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }
        //check duplicate email
        if(dbClient.emailExists(admin.getEmail())) {
            return new ResponseEntity<>("{\"Error\":  \"The email already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }

        String adminId = dbClient.saveAdmin(admin);
        //https://www.baeldung.com/jackson-object-mapper-tutorial
        ObjectMapper objectMapper = new ObjectMapper();
        AdminRegisterResponse adminRegisterResponse = new AdminRegisterResponse(adminId, request.getAdminName(), request.getEmail());
        return new ResponseEntity<>(objectMapper.writeValueAsString(adminRegisterResponse), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping(value = "/admins/{id}", produces = "application/json")
    public ResponseEntity<String> login(@PathVariable long id) throws JsonProcessingException {
        DBClient dbClient = new DBClient();
        Admin admin = dbClient.getAdminById(id);
        //check valid admin id
        if(admin == null) {
            return new ResponseEntity<>("{\"Error\":  \"The admin_id doesn't exist\"}", HttpStatus.NOT_FOUND);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        AdminLoginResponse adminLoginResponse = new AdminLoginResponse(admin.getId(), admin.getUserName(), admin.getEmail(), admin.getPets());
        return new ResponseEntity<>(objectMapper.writeValueAsString(adminLoginResponse), HttpStatus.OK);
    }
}
