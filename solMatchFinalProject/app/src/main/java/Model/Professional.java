package Model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Professional implements Serializable {

    String UID;
    String email;
    String userName;
    String imageUrl;
    String category;
    String address;
    String phoneNum;
    String description;
    String precAvailability;
    List<Review> reviews;

    public Professional(String email, String userNmae, String imageUrl, String category, String address, String phoneNum, String description, String precAvailability,String UID) {
        this.email = email;
        this.userName = userNmae;
        this.imageUrl = imageUrl;
        this.category = category;
        this.address = address;
        this.phoneNum = phoneNum;
        this.description = description;
        this.precAvailability = precAvailability;
        this.UID = UID;
    }
    public Professional(String email, String userNmae, String imageUrl, String category, String address, String phoneNum, String description, String precAvailability,String UID,List<Review> reviews) {
        this.email = email;
        this.userName = userNmae;
        this.imageUrl = imageUrl;
        this.category = category;
        this.address = address;
        this.phoneNum = phoneNum;
        this.description = description;
        this.precAvailability = precAvailability;
        this.UID = UID;
        this.reviews=reviews;
    }


    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Professional() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrecAvailability() {
        return precAvailability;
    }

    public void setPrecAvailability(String precAvailability) {
        this.precAvailability = precAvailability;
    }
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


}
