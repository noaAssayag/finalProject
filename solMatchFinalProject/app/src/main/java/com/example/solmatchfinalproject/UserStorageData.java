package com.example.solmatchfinalproject;

public class UserStorageData {
    private String UID;
    private String userName;
    private String email;
    private String gen;
    private String birthday;
    private String password;
    private String type;

    public UserStorageData(String UID,String userName, String email, String gen, String birthday,String password, String type) {
        this.UID = UID;
        this.userName = userName;
        this.email = email;
        this.gen = gen;
        this.birthday = birthday;
        this.password=password;
        this.type = type;

    }


    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
