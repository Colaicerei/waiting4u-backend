package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.*;
import edu.osu.waiting4ubackend.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDBClient {
    //user functions from here
    private static final String USERS_COLLECTION_NAME = "users";
    private Datastore db;

    public UserDBClient() {
        db = DatastoreOptions.getDefaultInstance().getService();
    }
    // functions for user from here
    public boolean userNameExists(String userName) {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind(USERS_COLLECTION_NAME)
                        .setFilter(StructuredQuery.PropertyFilter.eq("userName", userName))
                        .build();
        QueryResults<Entity> results = db.run(query);
        return results.hasNext();
    }

    public boolean userEmailExists(String email) {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind(USERS_COLLECTION_NAME)
                        .setFilter(StructuredQuery.PropertyFilter.eq("email", email))
                        .build();
        QueryResults<Entity> results = db.run(query);
        return results.hasNext();
    }

    public String userExists(User user) {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind(USERS_COLLECTION_NAME)
                        .setFilter(StructuredQuery.CompositeFilter
                        .and(StructuredQuery.PropertyFilter.eq("email", user.getEmail()),
                                StructuredQuery.PropertyFilter.eq("password", user.getPassword())))
                        .build();
        QueryResults<Entity> results = db.run(query);
        if(!results.hasNext()) {
            return null;
        } else {
            Entity entity = results.next();
            return entity.getKey().getId().toString();
        }
    }

    public String saveUser(User user) {
        Key key = db.allocateId(db.newKeyFactory().setKind(USERS_COLLECTION_NAME).newKey());
        Entity userEntity = Entity.newBuilder(key)
                .set("userName", user.getUserName())
                .set("email", user.getEmail())
                .set("password", user.getPassword())
                .set("introduction", user.getIntroduction())
                .set("preferences", DBClientHelper.convertToValueList(user.getPreferences()))
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
                .setUserName(userEntity.getString("userName"))
                .setEmail(userEntity.getString("email"))
                .setIntroduction((userEntity.getString("introduction")))
                .setPassword(userEntity.getString("password"))
                .setPreferences(DBClientHelper.convertToList(userEntity.getList("preferences")))
                .build();
    }

    public void updatePreferences(String newPreference, long id) {
        Key key = db.newKeyFactory().setKind(USERS_COLLECTION_NAME).newKey(id);
        Entity userEntity = db.get(key);
        List<Value<String>> valueList = userEntity.getList("preferences");
        List<Value<String>> list = new ArrayList<>(valueList);
        list.add(StringValue.of(newPreference));
        userEntity = Entity.newBuilder(db.get(key)).set("preferences", list).build();
        db.update(userEntity);
    }

    public String updateUser(User user, long id) {
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        Key key = db.newKeyFactory().setKind(USERS_COLLECTION_NAME).newKey(id);
        Entity userEntity = db.get(key);
        if(userEntity == null) return null;

        if(user.getPassword() != null){
            userEntity = Entity.newBuilder(db.get(key))
                    .set("password", user.getPassword()).build();
            db.update(userEntity);
        }
        userEntity = Entity.newBuilder(db.get(key))
                .set("userName", user.getUserName())
      //          .set("email", user.getEmail())
                .set("introduction", user.getIntroduction())
      //          .set("preferences", DBClientHelper.convertToValueList(user.getPreferences()))
                .build();
        db.update(userEntity);

        return key.getId().toString();
    }

}
