package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.request.ContactRequest;
import edu.osu.waiting4ubackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class FormController {
    @Autowired
    EmailService emailService;

    @CrossOrigin
    @PostMapping(value = "/contactform", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> formPost(@Valid @RequestBody ContactRequest contactRequest, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return ControllerHelper.displayErrorMessage(errors);
        }

        String subject = contactRequest.getName() + " " + " sent a message";
        if (contactRequest.getPetName() != null && !contactRequest.getPetName().trim().isEmpty()) {
            subject += " regarding " + contactRequest.getPetName();
        }
        emailService.sendContactForm(subject, contactRequest.getEmail(), contactRequest.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(contactRequest), HttpStatus.OK);
    }
}
