package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;
import edu.osu.waiting4ubackend.entity.Pet;
import edu.osu.waiting4ubackend.response.GetUpdatesResponse;

import java.util.ArrayList;
import java.util.List;

//https://stackoverflow.com/questions/48389765/google-cloud-datastore-api-how-to-add-string-array-list
//https://developers.google.com/protocol-buffers/docs/reference/java/com/google/protobuf/StringValue
public class DBClientHelper {
    public static List<Value<String>> convertToValueList(List<String> list) {
        if(list == null) return null;
        List<Value<String>> result = new ArrayList<>();
        for (String s : list) {
            result.add(StringValue.of(s));
        }
        return result;
    }

    public static List<String> convertToList(List<Value<String>> valueList) {
        if(valueList == null) return null;
        List<String> list = new ArrayList<>();
        for(Value<String> v : valueList) {
            list.add(v.get());
        }
        return list;
    }

    public static void populateQueryResults(List<Pet> list, QueryResults<Entity> results) {
        while (results.hasNext()) {
            Entity petEntity = results.next();
            Pet pet = new Pet.PetBuilder()
                    .setId(petEntity.getKey().getId().toString())
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
                    .setAdminId(petEntity.getString("adminId").toString())
                    .setImageUrl(petEntity.getString("imageUrl"))
                    .build();
            list.add(pet);
        }
    }

    public static List<Value<String>> removePetId(List<Value<String>> list, String id) {
        List<Value<String>> newList = new ArrayList<>();
        for(Value<String> v : list) {
            if(!v.get().equals(id)) {
                newList.add(v);
            }
        }
        return newList;
    }

    public static void populateStatusResults(List<GetUpdatesResponse> list, QueryResults<Entity> results) {
        int count = 0;
        while (results.hasNext() && count < 3) {
            Entity petEntity = results.next();
            String id = petEntity.getKey().getId().toString();
            String petName = petEntity.getString("petName");
            String imageUrl = petEntity.getString("imageUrl");
            List<String> statusList = DBClientHelper.convertToList(petEntity.getList("status"));
            List<String> status = new ArrayList<>();
            if(statusList.size() != 0) {
                status.add(statusList.get(statusList.size() - 1));
            }
            GetUpdatesResponse getUpdatesResponse = new GetUpdatesResponse(id, petName, status, imageUrl);
            list.add(getUpdatesResponse);
            count++;
        }
    }
}
