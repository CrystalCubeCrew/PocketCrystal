package edu.temple.crystalcube11;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongear on 4/22/17.
 */

@IgnoreExtraProperties
public class ToDoList {


    public String task;
    public String nodeKey;


    public ToDoList() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public ToDoList(String task, String nodeKey) {
        this.task = task;
        this.nodeKey = nodeKey;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("task", task);

        return result;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
}
