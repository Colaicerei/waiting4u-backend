package edu.osu.waiting4ubackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetUpdatesResponse {
    @JsonProperty("pet_id")
    private String petId;
    @JsonProperty("pet_name")
    private String petName;
    private List<String> status;
    @JsonProperty("image_url")
    private String imageUrl;

    public GetUpdatesResponse(String petId, String petName, List<String> status, String imageUrl) {
        this.petId = petId;
        this.petName = petName;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public String getPetId() {
        return petId;
    }

    public String getPetName() {
        return petName;
    }

    public List<String> getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
