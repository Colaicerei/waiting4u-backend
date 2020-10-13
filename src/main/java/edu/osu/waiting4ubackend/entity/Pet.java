package edu.osu.waiting4ubackend.entity;

import org.joda.time.LocalDate;
import java.util.List;

/**
 * Entity for Pet class
 */
public class Pet {
    private String id;
    private String petName;
    private String type;
    private String breed;
    private LocalDate dateCreated;
    private String availability;
    private String status;
    private String description;
    private List<String> dispositions;
    private String imageUrl;

    private Pet(PetBuilder petBuilder) {
        this.id = petBuilder.id;
        this.petName = petBuilder.petName;
        this.type = petBuilder.type;
        this.breed = petBuilder.breed;
        this.dateCreated = petBuilder.dateCreated;
        this.availability = petBuilder.availability;
        this.status = petBuilder.status;
        this.description = petBuilder.description;
        this.dispositions = petBuilder.dispositions;
        this.imageUrl = petBuilder.imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getPetName() {
        return petName;
    }

    public String getType() {
        return type;
    }

    public String getBreed() {
        return breed;
    }

    public String getAvailability() {
        return availability;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getDispositions() {
        return dispositions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static class PetBuilder {
        private String id;
        private String petName;
        private String type;
        private String breed;
        private LocalDate dateCreated;
        private String availability;
        private String status;
        private String description;
        private List<String> dispositions;
        private String imageUrl;

        public PetBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public PetBuilder setPetName(String petName) {
            this.petName = petName;
            return this;
        }

        public PetBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public PetBuilder setBreed(String breed) {
            this.breed = breed;
            return this;
        }

        public PetBuilder setDateCreated(LocalDate dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public PetBuilder setAvailability(String availability) {
            this.availability = availability;
            return this;
        }

        public PetBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public PetBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public PetBuilder setDispositions(List<String> dispositions) {
            this.dispositions = dispositions;
            return this;
        }

        public PetBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Pet build() {
            return new Pet(this);
        }
    }
}

