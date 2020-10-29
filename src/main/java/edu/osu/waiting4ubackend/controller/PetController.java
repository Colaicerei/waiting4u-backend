package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.PetDBClient;
import edu.osu.waiting4ubackend.entity.Pet;
import edu.osu.waiting4ubackend.request.PetRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PetController {
    @CrossOrigin
    @PostMapping(value = "/admin/{id}/pets", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createPet(@PathVariable long id, @RequestBody PetRequest request) throws JsonProcessingException {

        Pet pet = new Pet.PetBuilder()
                .setPetName(request.getPetName())
                .setDateOfBirth(request.getDateOfBirth())
                .setType(request.getType())
                .setBreed(request.getBreed())
                .setDescription(request.getDescription())
                .setAdminId(String.valueOf(id))
                .setImageUrl(request.getImageUrl())
                .build();

        PetDBClient petDBClient = new PetDBClient();
        Pet petResponse = petDBClient.savePet(pet);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(petResponse), HttpStatus.CREATED);

    }
}
