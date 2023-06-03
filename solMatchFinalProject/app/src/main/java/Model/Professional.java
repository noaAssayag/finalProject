package Model;

public class Professional {
    String email;
    String userNmae;
    String imageUrl;
    String category;
    String address;
    String phoneNum;
    String description;
    String precAvailability;

    public Professional(String email, String userNmae, String imageUrl, String category, String address, String phoneNum, String description, String precAvailability) {
        this.email = email;
        this.userNmae = userNmae;
        this.imageUrl = imageUrl;
        this.category = category;
        this.address = address;
        this.phoneNum = phoneNum;
        this.description = description;
        this.precAvailability = precAvailability;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserNmae() {
        return userNmae;
    }

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
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
}
