package com.example.solmatchfinalproject.ChatClasses;


public class chatItemInfo {
    private String name;
    private String message;
    private int imgId;

    public chatItemInfo(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    public chatItemInfo(String name,String message)
    {
        this.name = name;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
