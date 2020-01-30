package chatapp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private int serverPort;
    private InetAddress inetAddress;


    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    

    public Client(String address, int serverPort){
        this.serverPort = serverPort;
        try{
            this.inetAddress = InetAddress.getByName(address);
        }catch(UnknownHostException uhe){
            System.err.println(uhe.getMessage());
        }
    }

    public Client(InetAddress inetAddress, int serverPort){
        this.serverPort = serverPort;
        this.inetAddress = inetAddress;
    }


    public void start() {

        try{
            socket = new Socket(inetAddress, serverPort);
            din = new DataInputStream(socket.getInputStream()); 
            dout = new DataOutputStream(socket.getOutputStream()); 

            System.out.println("Client Started.");
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }


    public void sendMessage(String message){

        new Thread(new Runnable()  
        { 
            @Override
            public void run() {     
                    try { 
                        dout.writeUTF(message); 
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
                        String msg = din.readUTF(); 
                        System.out.println(msg); 
                    } catch (IOException e) { 
  
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }).start(); 
    }

    public static void main(String[] args) {
        Client client1 = new Client("localhost",3050);
        Client client2 = new Client("localhost",3050);
           
        client1.start();
        client2.start(); 

        client1.readMessage();
        client2.readMessage();

    }

}