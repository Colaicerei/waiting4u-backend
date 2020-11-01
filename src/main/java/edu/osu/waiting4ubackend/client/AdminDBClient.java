package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.Admin;

import java.util.ArrayList;
import java.util.List;

//https://cloud.google.com/datastore/docs/reference/libraries
//https://cloud.google.com/datastore/docs/concepts/queries
public class AdminDBClient {
    private static final String ADMINS_COLLECTION_NAME = "admins";
    private Datastore db;

    public AdminDBClient() {
        db = DatastoreOptions.getDefaultInstance().getService();
    }

    public boolean userNameExists(String userName) {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind(ADMINS_COLLECTION_NAME)
                        .setFilter(StructuredQuery.PropertyFilter.eq("userName", userName))
                        .build();
        QueryResults<Entity> results = db.run(query);
        return results.hasNext();
    }

    public boolean emailExists(String email) {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind(ADMINS_COLLECTION_NAME)
                        .setFilter(StructuredQuery.PropertyFilter.eq("email", email))
                        .build();
        QueryResults<Entity> results = db.run(query);
        return results.hasNext();
    }

    public String adminExists(Admin admin) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ADMINS_COLLECTION_NAME)
                .setFilter(StructuredQuery.CompositeFilter
                        .and(StructuredQuery.PropertyFilter.eq("email", admin.getEmail()),
                                StructuredQuery.PropertyFilter.eq("password", admin.getPassword())))
                .build();
        QueryResults<Entity> results = db.run(query);
        if(!results.hasNext()) {
            return null;
        } else {
            Entity entity = results.next();
            return entity.getKey().getId().toString();
        }
    }

    public String saveAdmin(Admin admin) {
        Key key = db.allocateId(db.newKeyFactory().setKind(ADMINS_COLLECTION_NAME).newKey());
        Entity adminEntity = Entity.newBuilder(key)
                .set("userName", admin.getUserName())
                .set("email", admin.getEmail())
                .set("password", admin.getPassword())
                .set("pets", DBClientHelper.convertToValueList(admin.getPets()))
                .build();
        db.put(adminEntity);

        return key.getId().toString();
    }

    public Admin getAdminById(long id) {
        Key key = db.newKeyFactory().setKind(ADMINS_COLLECTION_NAME).newKey(id);
        Entity adminEntity = db.get(key);
        if(adminEntity == null) return null;
        return new Admin.AdminBuilder()
                .setId(key.getId().toString())
                .setUserName(adminEntity.getString("userName"))
                .setEmail(adminEntity.getString("email"))
                .setPets(DBClientHelper.convertToList(adminEntity.getList("pets")))
                .build();
    }

    public void updatePetEntity(String petId, long adminId) {
        Key key = db.newKeyFactory().setKind(ADMINS_COLLECTION_NAME).newKey(adminId);
        Entity adminEntity = db.get(key);
        List<Value<String>> valueList = adminEntity.getList("pets");
//        System.out.println("hello");
        List<Value<String>> list = new ArrayList<>(valueList);
        list.add(StringValue.of(petId));
        adminEntity = Entity.newBuilder(db.get(key)).set("pets", list).build();
        db.update(adminEntity);
    }

    public void removePetByAdmin(long adminId, long petId) {
        Key key = db.newKeyFactory().setKind(ADMINS_COLLECTION_NAME).newKey(adminId);
        Entity adminEntity = db.get(key);
        List<Value<String>> valueList = adminEntity.getList("pets");
        List<Value<String>> newList = DBClientHelper.removePetId(valueList, String.valueOf(petId));
        adminEntity = Entity.newBuilder(db.get(key)).set("pets", newList).build();
        db.update(adminEntity);
    }
}