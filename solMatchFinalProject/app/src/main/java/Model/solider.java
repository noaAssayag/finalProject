package Model;

import java.sql.Date;

public class solider extends users{

    private String originCountry;

    public solider(int ID, String fullName, int phoneNum, String adress, Date birthDate, String originCountry) {
        super(ID, fullName, phoneNum, adress, birthDate);
        this.originCountry = originCountry;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }
}
