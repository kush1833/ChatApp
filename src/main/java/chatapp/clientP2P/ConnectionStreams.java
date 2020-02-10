package chatapp.clientP2P;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionStreams {

    private ObjectInputStream din;
    private ObjectOutputStream dout;
    private String username;

    ConnectionStreams(String username, Socket s) {
        this.username = username;
        try {
            din = new ObjectInputStream(s.getInputStream());
            dout = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ObjectInputStream getDIN(){
        return this.din;
    }

    public ObjectOutputStream getDOUT(){
        return this.dout;
    }

}