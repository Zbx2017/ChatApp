package Model;

public class Chat {
    public String sender;
    public String receiver;
    public String message;
    private boolean isseen;
    private boolean isaudio;


    public Chat(String sender, String receiver, String message,boolean isseen, boolean isaudio) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
        this.isaudio = isaudio;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public boolean isIsaudio() {
        return isaudio;
    }

    public void setIsaudio(boolean isaudio) {
        this.isaudio = isaudio;
    }
}
