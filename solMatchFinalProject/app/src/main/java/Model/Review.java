package Model;

public class Review {
    private String UID;
    private String nameReview;
    private float rate;
    private String comments;

    public Review(String nameReview, float rate, String comments,String UID) {
        this.nameReview = nameReview;
        this.rate = rate;
        this.comments = comments;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getNameReview() {
        return nameReview;
    }

    public void setNameReview(String nameReview) {
        this.nameReview = nameReview;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
