package Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

import Model.UserStorageData;

public class Host implements Serializable {
    private String uid;
    private String hostImg;
    private String hostName;
    private String hostEmail;
    private String hostAddress;
    private String hostingDate;
    private String hostingLocImg;
    private String description;
    private String accommodation="false";
    private String pets="false";
    private String privateRoom="false";
    private String secureEnv="false";
    private List<UserStorageData> listOfResidents;

    public Host(String hostImg, String hostName, String hostEmail, String hostAddress, String hostingDate, String hostingLocImg) {
        this.hostImg = hostImg;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostAddress = hostAddress;
        this.hostingDate = hostingDate;
        this.hostingLocImg = hostingLocImg;
    }

    public Host(String hostImg, String hostName, String hostEmail, String hostAddress, String hostingDate, String hostingLocImg, List<UserStorageData> listOfResidents) {
        this.hostImg = hostImg;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostAddress = hostAddress;
        this.hostingDate = hostingDate;
        this.hostingLocImg = hostingLocImg;
        this.listOfResidents = listOfResidents;
    }



    public Host() {

    }

    public Host(String hostImg, String hostName, String hostEmail, String hostAddress, String hostingDate, String hostingLocImg, String description, String accommodation, String pets, String privateRoom, String secureEnv) {
        this.hostImg = hostImg;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostAddress = hostAddress;
        this.hostingDate = hostingDate;
        this.hostingLocImg = hostingLocImg;
        this.description = description;
        this.accommodation = accommodation;
        this.pets = pets;
        this.privateRoom = privateRoom;
        this.secureEnv = secureEnv;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getPrivateRoom() {
        return privateRoom;
    }

    public void setPrivateRoom(String privateRoom) {
        this.privateRoom = privateRoom;
    }

    public String getSecureEnv() {
        return secureEnv;
    }

    public void setSecureEnv(String secureEnv) {
        this.secureEnv = secureEnv;
    }

    public List<UserStorageData> getListOfResidents() {
        return listOfResidents;
    }

    public void setListOfResidents(List<UserStorageData> listOfResidents) {
        this.listOfResidents = listOfResidents;
    }
}