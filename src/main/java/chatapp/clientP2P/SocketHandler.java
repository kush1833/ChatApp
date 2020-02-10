package chatapp.clientP2P;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import chatapp.message.Message;

public class SocketHandler {

    private ServerSocket serverSocket;
    private Socket socket;

    private ObjectOutputStream dout;


    private int port;

    private Map<String, Socket> openSockets;
    private ClientConnectionListener listener;

    public SocketHandler(int port) {
        this.port = port;
        this.openSockets = new HashMap<>();
    }


    public void addClientConnectionListener(ClientConnectionListener listener){
        this.listener = listener;
    }



    public void connectSocket(String username, String srcUsername ,int port) {

        try {
            Socket socket = new Socket("localhost", port);
            // ConnectionStreams conn = new ConnectionStreams(srcUsername,socket);
            readMessage(socket);
            openSockets.put(srcUsername,socket);
            this.listener.clientSocketAdded(srcUsername, socket);
            Message message = new Message(username,"unknown","hello");
            sendMessage(socket, message);
            printOpenSockets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendMessage(Socket socket, Message message) {

        new Thread(new Runnable() {
        
            @Override
            public void run() {
                try {
                    dout = new ObjectOutputStream(socket.getOutputStream());
                    dout.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void readMessage(Socket socket) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean connected = false;
                Message message;
                try {
                    ObjectInputStream din = new ObjectInputStream(socket.getInputStream());
                    while (true) {

                        message = (Message) din.readObject();
                        System.out.println(port+" : "+message.getData());
                        if (!connected && message.getReceiver().equals("unknown")) {
                            connected = true;
                            openSockets.put(message.getSender(), socket);
                            listener.clientSocketAdded(message.getSender(),socket);
                            printOpenSockets();
                        } else {

                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void startServer() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    serverSocket = new ServerSocket(port);
                    while (true) {
                        socket = serverSocket.accept();
                        readMessage(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void printOpenSockets(){
        for(Map.Entry<String,Socket> mEntry : openSockets.entrySet()){
            System.out.println(port+" : "+mEntry.getKey()+" : "+mEntry.getValue());
        }
    }


}