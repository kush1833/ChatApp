package chatapp.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

import chatapp.message.Message;

public class ClientHandler implements Runnable {

    @SuppressWarnings("unused")
    private Socket socket;
    private ObjectInputStream din;
    private ObjectOutputStream dout;
    private Server context;
    private boolean isConnected;
    private String username;


    public ClientHandler(Server context, Socket socket)throws IOException{
        this.isConnected = false;
        this.context = context;
        this.username = "default";
        this.socket = socket;
        this.dout = new ObjectOutputStream(socket.getOutputStream());
        this.din = new ObjectInputStream(socket.getInputStream());
    }

  


    @Override
    public void run() {
        
        Message received;
        while (true)  
        { 
            try
            { 
                // receive the string 
                received = (Message) din.readObject();
                System.out.println(received.getSender()+" :"+received.getData());
                
                if(!this.isConnected && received.getReceiver().equals("server")){
                    this.isConnected = true;
                    this.username = received.getSender();
                    context.updateClient(this.username);
                    this.sendMessage(new Message("server",this.username,"hello"));
                }

            }catch(IOException e){
                System.err.println("Error: "+e.getMessage());
                break;
            }  
            catch(ClassNotFoundException e){
                System.err.println("Error: "+e.getMessage());
            } 
        }   
    }


    public void sendMessage(Message message){

        new Thread(new Runnable()  
        { 
            @Override
            public void run() {     
                    try { 
                        dout.writeObject(message); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
                }
        }).start(); 

    }

    @SuppressWarnings("unused")
    private void close(){
        try{ 
            this.din.close(); 
            this.dout.close(); 

        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    }

}