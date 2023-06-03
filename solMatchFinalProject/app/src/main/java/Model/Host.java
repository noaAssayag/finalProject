package Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

import Model.UserStorageData;

public class Host implements Serializable {
    private String hostImg;
    private String hostName;
    private String hostEmail;
    private String hostAddress;
    private String hostingDate;
    private String hostingLocImg;
    private String description;
    private boolean accommodation=false;
    private boolean pets=false;
    private boolean privateRoom=false;
    private boolean secureEnv=false;
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

    public Host(String hostImg, String hostName, String hostEmail, String hostAddress, String hostingDate, String hostingLocImg,String description, boolean accommodation, boolean pets, boolean privateRoom, boolean secureEnv) {
        this.hostImg = hostImg;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostAddress = hostAddress;
        this.hostingDate = hostingDate;
        this.hostingLocImg = hostingLocImg;
        this.description=description;
        this.accommodation = accommodation;
        this.pets = pets;
        this.privateRoom = privateRoom;
        this.secureEnv = secureEnv;
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
    public boolean isAccommodation() {
        return accommodation;
    }

    public void setAccommodation(boolean accommodation) {
        this.accommodation = accommodation;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isPrivateRoom() {
        return privateRoom;
    }

    public void setPrivateRoom(boolean privateRoom) {
        this.privateRoom = privateRoom;
    }

    public boolean isSecureEnv() {
        return secureEnv;
    }

    public void setSecureEnv(boolean secureEnv) {
        this.secureEnv = secureEnv;
    }

    public List<UserStorageData> getListOfResidents() {
        return listOfResidents;
    }

    public void setListOfResidents(List<UserStorageData> listOfResidents) {
        this.listOfResidents = listOfResidents;
    }
}