package com.example.solmatch;

import java.time.LocalDate;

public class Users {
    private String userName;
    private String email;
    private String password;
    private String birthDate;
    private UserType type;

    public Users(String userName, String email, String password, String birthDate, UserType type) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.type = type;
    }

    public Users() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
