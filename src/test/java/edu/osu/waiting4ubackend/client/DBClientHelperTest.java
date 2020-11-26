package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class DBClientHelperTest {

    @Test
    public void testConvertToValueListNull() {
        assertNull(DBClientHelper.convertToList(null));
    }

    @Test
    public void testConvertToValueListEmpty() {
        assertTrue(DBClientHelper.convertToList(new ArrayList<>()).isEmpty());
    }

    @Test
    public void testConvertToValueListHappyPath() {
        List<String> list = Arrays.asList("abc", "bcd");
        List<Value<String>> res = DBClientHelper.convertToValueList(list);
        assertEquals(2, res.size());
        assertEquals("abc", res.get(0).get());
        assertEquals("bcd", res.get(1).get());
    }

    @Test
    public void testConvertToListNull() {
        assertNull(DBClientHelper.convertToList(null));
    }

    @Test
    public void testConvertToListEmpty() {
        assertTrue(DBClientHelper.convertToList(new ArrayList<>()).isEmpty());
    }

    @Test
    public void testConvertToListHappyPath() {
        Value<String> value = new StringValue("abc");
        List<Value<String>> valueList = Collections.singletonList(value);
        List<String> res = DBClientHelper.convertToList(valueList);
        assertEquals(1, res.size());
        assertEquals("abc", res.get(0));
    }

    @Test
    public void testRemovePetIdNull() {
        assertNull(DBClientHelper.removePetId(null, "1"));
    }

    @Test
    public void testRemovePetIdEmpty() {
        assertTrue(DBClientHelper.removePetId(new ArrayList<>(), "1").isEmpty());
    }

    @Test
    public void testRemovePetIdIdNotExists() {
        Value<String> value = new StringValue("1");
        List<Value<String>> valueList = Collections.singletonList(value);
        List<Value<String>> res = DBClientHelper.removePetId(valueList, "2");
        assertEquals(1, res.size());
    }

    @Test
    public void testRemovePetIdIdExists() {
        Value<String> value = new StringValue("1");
        List<Value<String>> valueList = Collections.singletonList(value);
        List<Value<String>> res = DBClientHelper.removePetId(valueList, "1");
        assertEquals(0, res.size());
    }
}
