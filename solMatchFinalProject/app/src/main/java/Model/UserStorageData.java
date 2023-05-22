package Model;

import android.graphics.Bitmap;

public class UserStorageData {
    private int UID;
    private String userName;
    private String email;
    private String gen;
    private String birthday;
    private String password;
    private Bitmap image=null;
    private String type;

    public UserStorageData(String userName, String email, String gen, String birthday,String password,Bitmap image, String type) {
        this.userName = userName;
        this.email = email;
        this.gen = gen;
        this.birthday = birthday;
        this.password=password;
        this.image=image;
        this.type = type;
    }

    public UserStorageData() {

    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
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
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
