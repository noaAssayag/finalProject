package com.example.solmatchfinalproject.Hosts;

import android.graphics.Bitmap;
public class Host {
    private String hostImg;
    private String hostName;
    private String hostEmail;
    private String hostAddress;
    private String hostingDate;
    private String hostingLocImg;

    public Host(String hostImg, String hostName, String hostEmail, String hostAddress, String hostingDate,String hostingLocImg) {
        this.hostImg = hostImg;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostAddress = hostAddress;
        this.hostingDate = hostingDate;
        this.hostingLocImg=hostingLocImg;
    }

    public Host() {

    }


    public String getHostImg() {
        return hostImg;
    }

    public void setHostImg(String hostImg) {
        this.hostImg = hostImg;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getHostingDate() {
        return hostingDate;
    }

    public void setHostingDate(String hostingDate) {
        this.hostingDate = hostingDate;
    }

    public String getHostingLocImg() {
        return hostingLocImg;
    }

    public void setHostingLocImg(String hostingLocImg) {
        this.hostingLocImg = hostingLocImg;
    }
}