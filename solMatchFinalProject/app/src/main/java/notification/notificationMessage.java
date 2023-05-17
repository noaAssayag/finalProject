package notification;

public class notificationMessage {
    public static String message = "{" +
            "  \"to\": \"/topics/%s\"," +
            "  \"notification\": {" +
            "       \"body\":\"%s\"," +
            "       \"title\":\"%s\""+
            "   }" +
            "}";
}
