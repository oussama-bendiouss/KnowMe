package com.sociale.knowme.Models;

public class User {
    String uid;
    String name,email;
    public User(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "User{"+"uid='"+name+'\''+",email='"+email+'\''+'}';
    }
}

