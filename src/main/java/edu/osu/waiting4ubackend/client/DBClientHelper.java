package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;

import java.util.ArrayList;
import java.util.List;

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
        for(Value v : valueList) {
            list.add(v.toString());
        }
        return list;
    }
}
