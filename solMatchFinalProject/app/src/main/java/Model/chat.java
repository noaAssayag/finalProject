package Model;

public class chat {
    private int ID;
    private users userSent;
    private users userRecived;
    private String content;

    public chat(int ID, users userSent, users userRecived, String content) {
        this.ID = ID;
        this.userSent = userSent;
        this.userRecived = userRecived;
        this.content = content;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public users getUserSent() {
        return userSent;
    }

    public void setUserSent(users userSent) {
        this.userSent = userSent;
    }

    public users getUserRecived() {
        return userRecived;
    }

    public void setUserRecived(users userRecived) {
        this.userRecived = userRecived;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
