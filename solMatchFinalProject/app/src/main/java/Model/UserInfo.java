package Model;

public class UserInfo {
    private int title;
    private String value;

    public UserInfo(int title, String value) {
        this.title = title;
        this.value = value;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
