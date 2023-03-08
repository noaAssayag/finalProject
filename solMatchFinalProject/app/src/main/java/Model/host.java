package Model;

import java.sql.Date;

public class host extends users{

    private String type;

    public host(int ID, String fullName, int phoneNum, String adress, Date birthDate, String type) {
        super(ID, fullName, phoneNum, adress, birthDate);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
