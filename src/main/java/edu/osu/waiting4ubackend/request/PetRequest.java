package edu.osu.waiting4ubackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class PetRequest {
    @JsonProperty("pet_name")
    private String petName;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    private String type;
    private String breed;
    private List<String> dispositions;
    private String description;
    @JsonProperty("image_url")
    private String imageUrl;

    public String getPetName() {
        return petName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getType() {
        return type;
    }

    public String getBreed() {
        return breed;
    }

    public List<String> getDispositions() { return dispositions; }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() { return imageUrl; }
}
