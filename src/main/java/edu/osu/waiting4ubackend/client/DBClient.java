package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.Admin;
import edu.osu.waiting4ubackend.entity.User;

import javax.management.QueryEval;
import java.io.IOException;

//https://cloud.google.com/datastore/docs/reference/libraries
//https://cloud.google.com/datastore/docs/concepts/queries
public class DBClient {
    private static final String ADMINS_COLLECTION_NAME = "admins";
    //user functions from here
    private static final String USERS_COLLECTION_NAME = "users";
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

    // functions for user from here
    public boolean user_nameExists(String userName) {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind(USERS_COLLECTION_NAME)
                        .setFilter(StructuredQuery.PropertyFilter.eq("user_name", userName))
                        .build();
        QueryResults<Entity> results = db.run(query);
        return results.hasNext();
    }

    public boolean user_emailExists(String email) {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind(USERS_COLLECTION_NAME)
                        .setFilter(StructuredQuery.PropertyFilter.eq("email", email))
                        .build();
        QueryResults<Entity> results = db.run(query);
        return results.hasNext();
    }

    public String saveUser(User user) {
        Key key = db.allocateId(db.newKeyFactory().setKind(USERS_COLLECTION_NAME).newKey());
        Entity userEntity = Entity.newBuilder(key)
                .set("user_name", user.getUserName())
                .set("email", user.getEmail())
                .set("password", user.getPassword())
                .set("introduction", user.getIntroduction())
                .build();
        db.put(userEntity);

        return key.getId().toString();
    }

    public User getUserById(long id) {
        Key key = db.newKeyFactory().setKind(USERS_COLLECTION_NAME).newKey(id);
        Entity userEntity = db.get(key);
        if(userEntity == null) return null;
        return new User.UserBuilder()
                .setId(key.getId().toString())
                .setUserName(userEntity.getString("user_name"))
                .setEmail(userEntity.getString("email"))
                .setIntroduction((userEntity.getString("introduction")))
                .build();
    }
}