package edu.osu.waiting4ubackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osu.waiting4ubackend.client.AdminDBClient;
import edu.osu.waiting4ubackend.client.PetDBClient;
import edu.osu.waiting4ubackend.entity.Admin;
import edu.osu.waiting4ubackend.entity.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class PetController {
    @CrossOrigin
    @PostMapping(value = "/admins/{id}/pets", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createPet(@PathVariable long id, @RequestBody Pet petRequest) throws JsonProcessingException {

        Pet newPet = new Pet.PetBuilder()
                .setPetName(petRequest.getPetName())
                .setDateOfBirth(petRequest.getDateOfBirth())
                .setDateCreated(new Date())
                .setType(petRequest.getType())
                .setBreed(petRequest.getBreed())
                .setAvailability(petRequest.getAvailability())
                .setStatus(petRequest.getStatus())
                .setDescription(petRequest.getDescription())
                .setDispositions(petRequest.getDispositions())
                .setAdminId(String.valueOf(id))
                .setImageUrl(petRequest.getImageUrl())
                .build();

        PetDBClient petDBClient = new PetDBClient();
        //save pet into database
        Pet petResponse = petDBClient.savePet(newPet);
        //update the pet field in admin table
        AdminDBClient adminDBClient = new AdminDBClient();
        adminDBClient.updatePetEntity(petResponse.getId(), id);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(petResponse), HttpStatus.CREATED);

    }
    /*

     */
    @CrossOrigin
    @GetMapping(value = "/pets", produces = "application/json")
    public ResponseEntity<String> getPets() throws JsonProcessingException {
        PetDBClient petDBClient = new PetDBClient();
        List<Pet> petList = petDBClient.getPets();
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(petList), HttpStatus.OK);
    }
    /*
     * Method: getPetsByAdmin
     * RequestBody: N/A
     * PathVariable: adminId
     * Description: A admin sent get request to retrieve pets information which are created by this admin
     * Return: A array of Json Object
     */
    @CrossOrigin
    @GetMapping(value = "/admins/{id}/pets", produces = "application/json")
    public ResponseEntity<String> getPetsByAdmin(@PathVariable long id) throws JsonProcessingException {
        //check valid admin id
        AdminDBClient adminDbClient = new AdminDBClient();
        Admin admin = adminDbClient.getAdminById(id);
        if(admin == null) {
            return new ResponseEntity<>("{\"Error\":  \"Unauthorized user\"}", HttpStatus.NOT_FOUND);
        }
        PetDBClient petDBClient = new PetDBClient();
        List<Pet> petList = petDBClient.getPetsByAdmin(String.valueOf(id));
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(petList), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/admins/{admin_id}/pets/{pet_id}", produces = "application/json")
    public ResponseEntity<String> getPet(@PathVariable("admin_id") long adminId, @PathVariable("pet_id")long petId) throws JsonProcessingException {
        //check valid pet id
        PetDBClient petDBClient = new PetDBClient();
        //check valid pet id which associates with admin id
        Pet pet = petDBClient.getPetById(petId);
        if(pet == null) {
            return new ResponseEntity<>("{\"Error\":  \"Pet not found\"}", HttpStatus.NOT_FOUND);
        }
        if(!pet.getAdminId().equals(String.valueOf(adminId))) {
            return new ResponseEntity<>("{\"Error\":  \"Unauthorized admin\"}", HttpStatus.UNAUTHORIZED);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return new ResponseEntity<>(objectMapper.writeValueAsString(pet), HttpStatus.OK);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/admins/{admin_id}/pets/{pet_id}")
    public ResponseEntity<String> deletePet(@PathVariable("admin_id") long adminId, @PathVariable("pet_id")long petId) {
        //check valid pet id
        PetDBClient petDBClient = new PetDBClient();
        //check valid pet id which associates with admin id
        Pet pet = petDBClient.getPetById(petId);
        if(pet == null) {
            return new ResponseEntity<>("{\"Error\":  \"Pet not found\"}", HttpStatus.NOT_FOUND);
        }
        if(!pet.getAdminId().equals(String.valueOf(adminId))) {
            return new ResponseEntity<>("{\"Error\":  \"Unauthorized admin\"}", HttpStatus.UNAUTHORIZED);
        }
        //remove pet_id from pets entity of admin
        AdminDBClient adminDBClient = new AdminDBClient();
        adminDBClient.removePetInAdminEntity(adminId, petId);
        //delete pet
        petDBClient.deletePetById(petId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @CrossOrigin
    @PatchMapping(value = "/admins/{admin_id}/pets/{pet_id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updatePet(@PathVariable("admin_id") long adminId, @PathVariable("pet_id")long petId, @RequestBody Pet petRequest) throws JsonProcessingException {
        //check valid pet id
        PetDBClient petDBClient = new PetDBClient();
        //check valid pet id which associates with admin id
        Pet pet = petDBClient.getPetById(petId);
        if(pet == null) {
            return new ResponseEntity<>("{\"Error\":  \"Pet not found\"}", HttpStatus.NOT_FOUND);
        }
        if(!pet.getAdminId().equals(String.valueOf(adminId))) {
            return new ResponseEntity<>("{\"Error\":  \"Unauthorized admin\"}", HttpStatus.UNAUTHORIZED);
        }

        //compare pet with current pet
        System.out.println(pet.getAdminId());
        System.out.println(pet.getDateCreated());

        Pet newPet = new Pet.PetBuilder()
                .setPetName(petRequest.getPetName())
                .setDateOfBirth(petRequest.getDateOfBirth())
                .setDateCreated(pet.getDateCreated())
                .setType(petRequest.getType())
                .setBreed(petRequest.getBreed())
                .setAvailability(petRequest.getAvailability())
                .setStatus(petRequest.getStatus())
                .setDescription(petRequest.getDescription())
                .setDispositions(petRequest.getDispositions())
                .setAdminId(pet.getAdminId())
                .setImageUrl(petRequest.getImageUrl())
                .build();


        Pet petResponse = petDBClient.updatePet(newPet, petId);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(petResponse), HttpStatus.OK);

    }
}
