package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PetResponse {
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
    private String description;
    private String adminId;
    private List<String> dispositions;
    @JsonProperty("image_url")
    private String imageUrl;

    public String getId() {
        return id;
    }


}
