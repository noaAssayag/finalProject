package com.example.solmatchfinalproject;

public class notifications {

    private String id;
    private String message;


    public notifications(String id, String message) {
        this.id = id;
        this.message = message;
    }
    public notifications()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
