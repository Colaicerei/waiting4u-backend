package edu.osu.waiting4ubackend.client;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.Pet;
import sun.security.krb5.internal.PAEncTSEnc;

import java.util.ArrayList;
import java.util.List;

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
                .set("dateCreated", Timestamp.of(pet.getDateCreated()))
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

    public List<Pet> getPets() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(PETS_COLLECTION_NAME)
                .build();
        QueryResults<Entity> results = db.run(query);
        if(!results.hasNext()) {
            return null;
        } else {
            List<Pet> petList = new ArrayList<>();
            DBClientHelper.populateQueryResults(petList, results);
            return petList;
        }

    }

    public List<Pet> getPetsByAdmin(String adminId) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(PETS_COLLECTION_NAME)
                .setFilter(StructuredQuery.PropertyFilter.eq("adminId", adminId))
                .build();
        QueryResults<Entity> results = db.run(query);
        List<Pet> petList = new ArrayList<>();
        if (results.hasNext()) {
            DBClientHelper.populateQueryResults(petList, results);
        }
        return petList;
    }

//    public Pet getPetByAdmin(long petId, String adminId) {
//
//    }
}
