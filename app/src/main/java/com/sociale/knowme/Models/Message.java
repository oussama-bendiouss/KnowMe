package com.sociale.knowme.Models;

public class Message {
    String message;
    String name;
    String Key;
    public Message(){}
    public Message(String message, String name){
        this.message = message;
        this.name = name;
    }
    public void setKey(String key) {
        Key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return Key;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
    @Override
    public String toString() {
        return "Message{"+"message='"+message+'\''+",name='"+name+'\''+",key='"+Key+'\''+'}';
    }
}
