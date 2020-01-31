package chatapp.message;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;

    private String sender;
    private List<String> receiverList;
    private String receiver; 
    private String data;

    public Message(String sender, List<String> receiver,String data) {
        this.sender = sender;
        this.receiverList = receiver;
        this.data = data;
    }

    public Message(String sender, String receiver,String data) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
    }

    public String getSender(){
        return this.sender;
    }

    public List<String> getReceivers(){
        return this.receiverList;
    }

    public String getReceiver(){
        return this.receiver;
    }

    public String getData(){
        return this.data;
    }
}