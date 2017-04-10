package edu.temple.crystalcube11;

/**
 * Created by hongear on 4/10/17.
 */
public class User {

    public String userId;
    public String firstName;
    public String lastName;
    public String location;

    public  User() {

    }

    public User( String userId, String firstName, String lastName, String location) {

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }
}
