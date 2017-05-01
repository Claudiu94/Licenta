package com.mainPackage.util;

/**
 * Created by claudiu on 16.04.2017.
 */
public class User {
    private int id;
    private String lastName, firstName, email, password, userName;

    public User(Object id, Object firstName, Object lastName, Object email,
                Object userName, Object password) {
        this.id = (Integer) id;
        this.firstName = (String) firstName;
        this.lastName = (String) lastName;
        this.email = (String) email;
        this.userName = (String) userName;
        this.password = (String) password;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
