package Model;

public class Review {
    private String nameReview;
    private float rate;
    private String comments;

    public Review(String nameReview, float rate, String comments) {
        this.nameReview = nameReview;
        this.rate = rate;
        this.comments = comments;
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
