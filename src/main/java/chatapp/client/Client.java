package chatapp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


import chatapp.message.Message;

public class Client {

    private int serverPort;
    private InetAddress inetAddress;

    private String username;
    private boolean isConnected;

    private Socket socket;
    private ObjectInputStream din;
    private ObjectOutputStream dout;
    

    public Client(String address, int serverPort){
        this.isConnected = false;
        this.serverPort = serverPort;
        try{
            this.inetAddress = InetAddress.getByName(address);
        }catch(UnknownHostException uhe){
            System.err.println(uhe.getMessage());
        }
    }


    public Client(InetAddress inetAddress, int serverPort){
        this.isConnected = false;
        this.serverPort = serverPort;
        this.inetAddress = inetAddress;
    }


    public void setUsername(String username){
        this.username = username;
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

    public void readMessage(){

        new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
  
                while (true) {
                    try { 
                        Message message = (Message) din.readObject();
                        System.out.println(message.getSender()+" : "+message.getData());
                        if(!isConnected && message.getSender().equals("server")){
                            isConnected = true;
                            System.out.println(isConnected);
                        }
                        
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } catch(ClassNotFoundException cnf){

                    }
                } 
            } 
        }).start(); 
    }

    public void start() {

        try{
            socket = new Socket(inetAddress, serverPort);
            din = new ObjectInputStream(socket.getInputStream()); 
            dout = new ObjectOutputStream(socket.getOutputStream());
            this.readMessage();
            Message handshakeMessage = new Message(this.username,"server", "hello");
            this.sendMessage(handshakeMessage);
            while(!isConnected);
            
            System.out.println("Client Started.");

        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
    
}