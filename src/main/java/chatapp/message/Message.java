package chatapp.message;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;

    private String sender;
    private Set<String> receiversSet;
    private String receiver; 
    private String data;
    private File file;


    public Message(String sender, Set<String> receivers,String data) {
        this.sender = sender;
        this.receiversSet = receivers;
        this.data = data;
        this.receiver = null;
        this.file = null;
    }

    public Message(String sender, String receiver,String data) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
        this.receiversSet = null;
        this.file = null;
    }

    public Message(String sender, Set<String> receivers, File file){
        this.sender = sender;
        this.receiversSet = receivers;
        this.file = file;
        this.receiver = null;
    }

    public String getSender(){
        return this.sender;
    }

    public Set<String> getReceivers(){
        return this.receiversSet;
    }

    public String getReceiver(){
        return this.receiver;
    }

    public String getData(){
        return this.data;
    }

    public File getFile(){
        return this.file;
    }



    public boolean containsFile(){
        if(this.file!=null)
            return true;
        return false;
    }

    public boolean isMultiReceiver(){
        if(this.receiver != null)
            return false;
        return true;
    }
}