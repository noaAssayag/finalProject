package Model;

public class chat {

    private String from;
    private String to;
    private String message;

    public chat( String from, String to, String massage) {

        this.from = from;
        this.to = to;
        this.message = massage;
    }



    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String massage) {
        this.message = massage;
    }
}
