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
                .set("preference", "Weekly")
                .set("favoritePets", DBClientHelper.convertToValueList(user.getFavoritePets()))
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
                .setIntroduction(userEntity.getString("introduction"))
                .setPassword(userEntity.getString("password"))
                .setPreference(userEntity.getString("preference"))
                .setFavoritePets(DBClientHelper.convertToList(userEntity.getList("favoritePets")))
                .build();
    }

    /*public void updatePreferences(String newPreference, long id) {
        Key key = db.newKeyFactory().setKind(USERS_COLLECTION_NAME).newKey(id);
        Entity userEntity = db.get(key);
        List<Value<String>> valueList = userEntity.getList("preferences");
        List<Value<String>> list = new ArrayList<>(valueList);
        list.add(StringValue.of(newPreference));
        userEntity = Entity.newBuilder(db.get(key)).set("preferences", list).build();
        db.update(userEntity);
    }*/

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
       //         .set("userName", user.getUserName())
      //          .set("email", user.getEmail())
                .set("introduction", user.getIntroduction())
                .set("preference", user.getPreference())
                .build();
        db.update(userEntity);

        return key.getId().toString();
    }


    // get all user emails
    public List<String> getUserEmails(String preference) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(USERS_COLLECTION_NAME)
                .setFilter(StructuredQuery.PropertyFilter.eq("preference", preference))
                .build();
        QueryResults<Entity> results = db.run(query);
        if (!results.hasNext()) {
            return null;
        } else {
            List<String> userEmailList = new ArrayList<>();
            while (results.hasNext()) {
                Entity userEntity = results.next();
                String userEmail = userEntity.getString("email");
                userEmailList.add(userEmail);
            }
            return userEmailList;
        }
    }


    public void updateFavoritePets(long userId, String petId, String operation) {
        Key key = db.newKeyFactory().setKind(USERS_COLLECTION_NAME).newKey(userId);
        Entity userEntity = db.get(key);
        List<Value<String>> valueList = userEntity.getList("favoritePets");
        System.out.println(valueList);
        List<Value<String>> list = new ArrayList<>(valueList);

        // if pet is not in the list, add it to the list, otherwise remove it from the list
        if(operation.equals("remove") && list.contains(StringValue.of(petId))){
            List<Value<String>> newList = DBClientHelper.removePetId(valueList, petId);
            userEntity = Entity.newBuilder(db.get(key)).set("favoritePets", newList).build();
        }else if(operation.equals("add") && !list.contains(StringValue.of(petId))){
            list.add(StringValue.of(petId));
            userEntity = Entity.newBuilder(db.get(key)).set("favoritePets", list).build();
        }else{
            return;
        }

        db.update(userEntity);
    }
}
