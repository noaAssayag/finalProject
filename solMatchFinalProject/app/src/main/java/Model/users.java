package Model;

import java.sql.Date;

public class users {
    private int ID;
    private String fullName;
    private int phoneNum;
    private String adress;
    private Date birthDate;
    private userPersonalInfo info;
    public users(int ID, String fullName, int phoneNum, String adress, Date birthDate) {
        this.ID = ID;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.adress = adress;
        this.birthDate = birthDate;
    }

    public users(int ID, String fullName, int phoneNum, String adress, Date birthDate, userPersonalInfo info) {
        this.ID = ID;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.adress = adress;
        this.birthDate = birthDate;
        this.info = info;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public userPersonalInfo getInfo() {
        return info;
    }

    public void setInfo(userPersonalInfo info) {
        this.info = info;
    }
}
