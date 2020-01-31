package chatapp.message;

import java.io.Serializable;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;

    private String sender;
    private String reciever;
    private String data;

    public Message(String sender,String reciever,String data) {
        this.sender = sender;
        this.reciever = reciever;
        this.data = data;
    }

    public String getSender(){
        return this.sender;
    }

    public String getReciever(){
        return this.reciever;
    }

    public String getData(){
        return this.data;
    }
}