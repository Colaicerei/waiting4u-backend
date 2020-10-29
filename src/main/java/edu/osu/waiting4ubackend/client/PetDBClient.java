package edu.osu.waiting4ubackend.client;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.Pet;

public class PetDBClient {
    private static final String PETS_COLLECTION_NAME = "pets";
    private Datastore db;

    public PetDBClient() {
        db = DatastoreOptions.getDefaultInstance().getService();
    }

    public Pet savePet(Pet pet) {
        Key key = db.allocateId(db.newKeyFactory().setKind(PETS_COLLECTION_NAME).newKey());
        Entity petEntity = Entity.newBuilder(key)
                .set("petName", pet.getPetName())
                .set("dateOfBirth", Timestamp.of(pet.getDateOfBirth()))
                .set("dateCreated", Timestamp.now())
                .set("type", pet.getType())
                .set("breed", pet.getBreed())
                .set("availability", pet.getAvailability())
                .set("status", pet.getStatus())
                .set("description", pet.getDescription())
                .set("dispositions", DBClientHelper.convertToValueList(pet.getDispositions()))
                .set("adminId", pet.getAdminId())
                .set("imageUrl", pet.getImageUrl())
                .build();
        db.put(petEntity);
        //System.out.println(petEntity);
        return new Pet.PetBuilder()
                .setId(key.getId().toString())
                .setPetName(petEntity.getString("petName"))
                .setDateOfBirth(petEntity.getTimestamp("dateOfBirth").toDate())
                .setDateCreated(petEntity.getTimestamp("dateCreated").toDate())
                .setType(petEntity.getString("type"))
                .setBreed(petEntity.getString("breed"))
                .setAvailability(petEntity.getString("availability"))
                .setStatus(petEntity.getString("status"))
                .setDescription(petEntity.getString("description"))
                .setDispositions(DBClientHelper.convertToList(petEntity.getList("dispositions")))
                .setAdminId(petEntity.getString("adminId").toString())
                .setImageUrl(petEntity.getString("imageUrl"))
                .build();
    }
}
