package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CreatePetResponse {
    @JsonProperty("pet_id")
    private String id;
    @JsonProperty("pet_name")
    private String petName;
    private String type;
    private String breed;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("date_created")
    private Date dateCreated;
    private String availability;
    private String status;
    @JsonProperty("image_url")
    private String imageUrl;


}
