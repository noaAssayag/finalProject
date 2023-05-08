package com.example.solmatchfinalproject;

import android.graphics.Bitmap;

public class UserHosting {
    String name;
    String dateHost;



    String address;
    int img;

    public UserHosting(String name, int img, String dateHost,String address) {
        this.name = name;
        this.img = img;
        this.dateHost = dateHost;
        this.address=address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDateHost() {
        return dateHost;
    }

    public void setDateHost(String dateHost) {
        this.dateHost = dateHost;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
