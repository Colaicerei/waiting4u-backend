package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.Admin;
import edu.osu.waiting4ubackend.entity.User;

//https://cloud.google.com/datastore/docs/reference/libraries
//https://cloud.google.com/datastore/docs/concepts/queries
public class DBClient_user {
    private static final String USERS_COLLECTION_NAME = "users";
    private Datastore db;

    public DBClient_user() {
        db = DatastoreOptions.getDefaultInstance().getService();
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
