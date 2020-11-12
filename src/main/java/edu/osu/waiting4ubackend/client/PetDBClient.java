package edu.osu.waiting4ubackend.client;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.Pet;
import sun.security.krb5.internal.PAEncTSEnc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PetDBClient {
    public static final String PETS_COLLECTION_NAME = "pets";
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
                .set("dateUpdated", Timestamp.of(pet.getDateUpdated()))
                .set("type", pet.getType())
                .set("breed", pet.getBreed())
                .set("availability", pet.getAvailability())
                .set("status", DBClientHelper.convertToValueList(pet.getStatus()))
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
                .setDateUpdated(petEntity.getTimestamp("dateUpdated").toDate())
                .setType(petEntity.getString("type"))
                .setBreed(petEntity.getString("breed"))
                .setAvailability(petEntity.getString("availability"))
                .setStatus(DBClientHelper.convertToList(petEntity.getList("status")))
                .setDescription(petEntity.getString("description"))
                .setDispositions(DBClientHelper.convertToList(petEntity.getList("dispositions")))
                .setAdminId(petEntity.getString("adminId"))
                .setImageUrl(petEntity.getString("imageUrl"))
                .build();
    }

//    public List<Pet> getPets() {
//        Query<Entity> query = Query.newEntityQueryBuilder()
//                .setKind(PETS_COLLECTION_NAME)
//                .build();
//        QueryResults<Entity> results = db.run(query);
//        if(!results.hasNext()) {
//            return null;
//        } else {
//            List<Pet> petList = new ArrayList<>();
//            DBClientHelper.populateQueryResults(petList, results);
//            return petList;
//        }
//    }

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

    public Pet getPetById(long petId) {
        Key key = db.newKeyFactory().setKind(PETS_COLLECTION_NAME).newKey(petId);
        Entity petEntity = db.get(key);
        if(petEntity == null) return null;
        return new Pet.PetBuilder()
                .setId(key.getId().toString())
                .setPetName(petEntity.getString("petName"))
                .setDateOfBirth(petEntity.getTimestamp("dateOfBirth").toDate())
                .setDateCreated(petEntity.getTimestamp("dateCreated").toDate())
                .setDateUpdated(petEntity.getTimestamp("dateUpdated").toDate())
                .setType(petEntity.getString("type"))
                .setBreed(petEntity.getString("breed"))
                .setAvailability(petEntity.getString("availability"))
                .setStatus(DBClientHelper.convertToList(petEntity.getList("status")))
                .setDescription(petEntity.getString("description"))
                .setDispositions(DBClientHelper.convertToList(petEntity.getList("dispositions")))
                .setAdminId(petEntity.getString("adminId"))
                .setImageUrl(petEntity.getString("imageUrl"))
                .build();
    }

    public void deletePetById(long petId) {
        Key key = db.newKeyFactory().setKind(PETS_COLLECTION_NAME).newKey(petId);
        db.delete(key);
    }

    public Pet updatePet(Pet pet, long petId) {
        Key key = db.newKeyFactory().setKind(PETS_COLLECTION_NAME).newKey(petId);
        Entity petEntity = Entity.newBuilder(key)
                .set("petName", pet.getPetName())
                .set("dateOfBirth", Timestamp.of(pet.getDateOfBirth()))
                .set("dateCreated", Timestamp.of(pet.getDateCreated()))
                .set("dateUpdated", Timestamp.of(pet.getDateUpdated()))
                .set("type", pet.getType())
                .set("breed", pet.getBreed())
                .set("status", DBClientHelper.convertToValueList(pet.getStatus()))
                .set("availability", pet.getAvailability())
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
                .setDateUpdated(petEntity.getTimestamp("dateUpdated").toDate())
                .setType(petEntity.getString("type"))
                .setBreed(petEntity.getString("breed"))
                .setStatus(DBClientHelper.convertToList(petEntity.getList("status")))
                .setAvailability(petEntity.getString("availability"))
                .setDescription(petEntity.getString("description"))
                .setDispositions(DBClientHelper.convertToList(petEntity.getList("dispositions")))
                .setAdminId(petEntity.getString("adminId"))
                .setImageUrl(petEntity.getString("imageUrl"))
                .build();
    }

    public List<Pet> filter(Query<Entity> query) {
        QueryResults<Entity> results = db.run(query);
        if(!results.hasNext()) {
            return null;
        } else {
            List<Pet> petList = new ArrayList<>();
            DBClientHelper.populateQueryResults(petList, results);
            return petList;
        }
    }

    public List<String> updateStatus(List<String> newStatus, Date date, long petId) {
        Key key = db.newKeyFactory().setKind(PETS_COLLECTION_NAME).newKey(petId);
        Entity petEntity = db.get(key);
        petEntity = Entity.newBuilder(db.get(key))
                .set("status", DBClientHelper.convertToValueList(newStatus))
                .set("dateUpdated", Timestamp.of(date))
                .build();
        db.update(petEntity);
        return DBClientHelper.convertToList(petEntity.getList("status"));
    }

    public List<Pet> getLatestThreeUpdates() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(PETS_COLLECTION_NAME)
                .setOrderBy(StructuredQuery.OrderBy.desc("dateUpdated")).build();
        QueryResults<Entity> results = db.run(query);
        if(!results.hasNext()) {
            return null;
        } else {
            List<Pet> petList = new ArrayList<>();
            DBClientHelper.populateStatusResults(petList, results);
            return petList;
        }
    }
}
