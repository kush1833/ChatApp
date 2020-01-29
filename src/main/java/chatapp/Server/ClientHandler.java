package chatapp.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    public int id;

    @SuppressWarnings("unused")
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;

    public ClientHandler(int id, Socket socket, DataInputStream din, DataOutputStream dout){
        this.id = id;
        this.socket = socket;
        this.din = din;
        this.dout = dout;
    }

    @Override
    public void run() {
        
        String received; 
        while (true)  
        { 
            try
            { 
                // receive the string 
                received = din.readUTF(); 
                System.out.println(received); 

                if(received.equals("exit")){
                    this.close();
                    break;
                }

                for (ClientHandler client : Server.activeClients)  
                { 
                   if(client.id == this.id){
                       client.dout.writeUTF("Hello #"+this.id);
                       break;
                   } 
                }
            }catch(IOException e){
                System.err.println(e.getMessage());
            }   
        }   
    }

    private void close(){
        try{ 
            this.din.close(); 
            this.dout.close(); 

        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    }

}