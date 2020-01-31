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

    public String getUsername(){
        return this.username;
    }


    @Override
    public void run() {
        
        Message message;
        while (true)  
        { 
            try
            { 
                // receive the string 
                message = (Message) din.readObject();
                
                System.out.println(message.getSender()+" :"+message.getData());
                
                if(!this.isConnected && message.getReceiver().equals("server")){
                    this.isConnected = true;
                    this.username = message.getSender();
                    context.addClient(this);
                    this.sendMessage(new Message("server",this.username,"hello"));
                }else{

                    if(message.isMultiReceiver()){
                        for(String un : message.getReceivers()){
                            context.getClient(un).sendMessage(message);
                        }
                    }else{
                        context.getClient(message.getReceiver()).sendMessage(message);
                    }
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