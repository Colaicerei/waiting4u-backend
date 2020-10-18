package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.DBClient;
import edu.osu.waiting4ubackend.entity.Admin;
import edu.osu.waiting4ubackend.request.AdminRegisterRequest;
import edu.osu.waiting4ubackend.response.AdminRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class AdminController {
    //https://www.baeldung.com/spring-controllers#Boot
    //https://howtodoinjava.com/spring5/webmvc/controller-getmapping-postmapping/
    //https://stackoverflow.com/questions/24292373/spring-boot-rest-controller-how-to-return-different-http-status-codes
    @PostMapping(value = "/admins", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody AdminRegisterRequest request) throws IOException, ExecutionException, InterruptedException {
        Admin admin = new Admin(request.getAdminName(), request.getPassword(), request.getEmail());
        DBClient dbClient = new DBClient();
        //check duplicate userName
        if(dbClient.userNameExits(admin.getUserName())) {
            return new ResponseEntity<>("{\"Error\":  \"The name already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }
        //check duplicate email
        if(dbClient.emailExits(admin.getEmail())) {
            return new ResponseEntity<>("{\"Error\":  \"The email already exists, please use another one\"}", HttpStatus.FORBIDDEN);
        }

        String adminId = dbClient.saveAdmin(admin);
        //https://www.baeldung.com/jackson-object-mapper-tutorial
        ObjectMapper objectMapper = new ObjectMapper();
        AdminRegisterResponse adminRegisterResponse = new AdminRegisterResponse(adminId, request.getAdminName(), request.getEmail());
        return new ResponseEntity<>(objectMapper.writeValueAsString(adminRegisterResponse), HttpStatus.CREATED);
    }
}
