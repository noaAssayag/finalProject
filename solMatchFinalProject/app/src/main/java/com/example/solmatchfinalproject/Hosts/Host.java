package com.example.solmatchfinalproject.Hosts;

import android.graphics.Bitmap;
public class Host {
    private Bitmap hostImage;
    private String hostName;
    private String hostEmail;
    private String hostAddress;
    private String hostingDate;

    public Host(Bitmap hostImage, String hostName, String hostEmail, String hostAddress, String hostingDate) {
        this.hostImage = hostImage;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostAddress = hostAddress;
        this.hostingDate = hostingDate;
    }

    public Host() {

    }

    public Bitmap getHostImage() {
        return hostImage;
    }

    public void setHostImage(Bitmap hostImage) {
        this.hostImage = hostImage;
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
}