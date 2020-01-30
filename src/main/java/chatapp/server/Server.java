package chatapp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Vector;

import chatapp.interfaces.ClientListener;

public class Server {

    private static final int DEFAULT_PORT = 3050;

    private int port;
    private int clientCount;

    private ServerSocket serverSocket;
    private Socket socket;
    private ClientListener listener;

    private DataInputStream din;
    private DataOutputStream dout;

    public static Vector<ClientHandler> activeClients;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {

        this.clientCount = 0;
        activeClients = new Vector<>();
        this.port = port;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (Exception e) {
            System.out.println("Error Starting Server : " + e.getMessage());
            System.exit(1);
        }
    }

    public void attachClientListener(ClientListener listener){
        this.listener = listener;
    }

    public int getPort() {
        return this.port;
    }

    public void listen() throws IOException {

        System.out.println("Server Started at " + this.port);
        while (true) {
            socket = serverSocket.accept();
            System.out.println("Client Joined: " + socket.getInetAddress());

            clientCount++;

            dout = new DataOutputStream(socket.getOutputStream());
            din = new DataInputStream(socket.getInputStream());

            ClientHandler newClient = new ClientHandler(clientCount, socket, din, dout);
            activeClients.add(newClient);

            Thread clientThread = new Thread(newClient);
            clientThread.start();
            listener.update(newClient.id);
        }
    }

    public void stop(){
        
        activeClients.clear();
        this.clientCount = 0;
        try{
            this.din.close();
            this.dout.close();
            this.socket = null;
            this.serverSocket.close();
        }catch(IOException e){
            System.err.println("Error Closing Server");
        }
    }

}