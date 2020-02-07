package chatapp.server;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;



public class Server {

    private static final int DEFAULT_PORT = 3050;

    private int port;
    @SuppressWarnings("unused")
    private int clientCount;

    private ServerSocket serverSocket;
    private Socket socket;
    private ClientListener listener;

    public  Map<String, ClientHandler> activeClients;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {

        this.clientCount = 0;
        activeClients = new HashMap<>();
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


    public void addClient(ClientHandler client){
        
        String un = client.getUsername();
        this.activeClients.put(un,client);
        this.listener.update(un);
    }

    public ClientHandler getClient(String un){
        return this.activeClients.get(un);
    }


    public void listen() throws IOException {

        System.out.println("Server Started at " + this.port);
        while (true) {
            socket = serverSocket.accept();
            System.out.println("Client Joined: " + socket.getInetAddress());

            this.clientCount++;
            ClientHandler newClient = new ClientHandler(this, socket);
            Thread clientThread = new Thread(newClient);
            clientThread.start();
        }
    }


    public void stop(){
        
        activeClients.clear();
        this.clientCount = 0;
        try{
            this.socket = null;
            this.serverSocket.close();
        }catch(IOException e){
            System.err.println("Error Closing Server");
        }
    }

}