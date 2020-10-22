package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.Admin;

import java.io.IOException;

//https://cloud.google.com/datastore/docs/reference/libraries
//https://cloud.google.com/datastore/docs/concepts/queries
public class DBClient {
    private static final String ADMINS_COLLECTION_NAME = "admins";
    private Datastore db;

    public DBClient() {
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

    public String saveAdmin(Admin admin) {
        Key key = db.allocateId(db.newKeyFactory().setKind(ADMINS_COLLECTION_NAME).newKey());
        Entity adminEntity = Entity.newBuilder(key)
                .set("userName", admin.getUserName())
                .set("email", admin.getEmail())
                .set("password", admin.getPassword())
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
                .build();
    }
}
