package edu.temple.crystalcube11;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongear on 5/5/17.
 */
@IgnoreExtraProperties
public class Contacts {


    public String name;
    public String number;
    public String nodeKey;


    public Contacts() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Contacts(String name, String number, String nodeKey) {
        this.name = name;
        this.number = number;
        this.nodeKey = nodeKey;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("number", number);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
}

