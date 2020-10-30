package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.AdminDBClient;
import edu.osu.waiting4ubackend.client.PetDBClient;
import edu.osu.waiting4ubackend.entity.Pet;
import edu.osu.waiting4ubackend.request.PetRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {
    @CrossOrigin
    @PostMapping(value = "/admins/{id}/pets", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createPet(@PathVariable long id, @RequestBody PetRequest request) throws JsonProcessingException {

        Pet pet = new Pet.PetBuilder()
                .setPetName(request.getPetName())
                .setDateOfBirth(request.getDateOfBirth())
                .setType(request.getType())
                .setBreed(request.getBreed())
                .setDispositions(request.getDispositions())
                .setDescription(request.getDescription())
                .setAdminId(String.valueOf(id))
                .setImageUrl(request.getImageUrl())
                .build();

        PetDBClient petDBClient = new PetDBClient();
        //save pet into database
        Pet petResponse = petDBClient.savePet(pet);
        //update the pet field in admin table
        AdminDBClient adminDBClient = new AdminDBClient();
        adminDBClient.updatePetEntity(petResponse.getId(), id);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(petResponse), HttpStatus.CREATED);

    }

    @CrossOrigin
    @GetMapping(value = "pets", produces = "application/json")
    public ResponseEntity<String> getPets() throws JsonProcessingException {
        PetDBClient petDBClient = new PetDBClient();
        List<Pet> petList = petDBClient.getPets();
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(petList), HttpStatus.OK);
    }
}
