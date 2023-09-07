package Model;

import android.graphics.Bitmap;

public class UserStorageData {
    private String UID;
    private String userName;
    private String email;
    private String gen;
    private String birthday;
    private String password;
    private String image=null;
    private String type;
    private userPersonalInfo info;


    public UserStorageData(String UID,String userName, String email, String gen, String birthday,String password,String image, String type) {
        this.UID=UID;
        this.userName = userName;
        this.email = email;
        this.gen = gen;
        this.birthday = birthday;
        this.password=password;
        this.image=image;
        this.type = type;
    }
    public UserStorageData()
    {

    }

    public UserStorageData(String UID,String userName, String email, String gen, String birthday, String password, String image, String type, userPersonalInfo info) {
        this.UID = UID;
        this.userName = userName;
        this.email = email;
        this.gen = gen;
        this.birthday = birthday;
        this.password = password;
        this.image = image;
        this.type = type;
        this.info = info;
    }
    public UserStorageData(String UID,String userName, String email, String gen, String birthday, String password, String type) {
        this.UID = UID;
        this.userName = userName;
        this.email = email;
        this.gen = gen;
        this.birthday = birthday;
        this.password = password;
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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public userPersonalInfo getInfo() {
        return info;
    }

    public void setInfo(userPersonalInfo info) {
        this.info = info;
    }
}
