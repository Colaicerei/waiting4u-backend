package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

public class ControllerHelper {

    public static ResponseEntity<String> displayErrorMessage(Errors errors) {
        List<ConstraintViolation<?>> violationsList = new ArrayList<>();
        for (ObjectError e : errors.getAllErrors()) {
            violationsList.add(e.unwrap(ConstraintViolation.class));
        }
        String exceptionMessage = "{\"Error\": \"" ;
        for (ConstraintViolation<?> violation : violationsList) {
            exceptionMessage += violation.getPropertyPath() + ": " + violation.getMessage() + "\n";
        }
        exceptionMessage += "\"}";

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    public static List<String> petStatusHelper(List<String> petStatus, List<String> newStatus) {
        for(String status : newStatus) {
            if(status.length() != 0) {
                petStatus.add(status);
            }
        }
        return petStatus;
    }
}
