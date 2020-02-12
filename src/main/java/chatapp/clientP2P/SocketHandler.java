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

    private int port;

    private Map<String, Socket> openSockets;
    private Map<String, ObjectOutputStream> doutStreams;
    private ClientConnectionListener listener;
    private Peer context;

    public SocketHandler(Peer context , int port) {
        this.port = port;
        this.context = context;
        this.openSockets = new HashMap<>();
        this.doutStreams = new HashMap<>();
    }

    public void addClientConnectionListener(ClientConnectionListener listener) {
        this.listener = listener;
    }

    public void connectSocket(String username, String srcUsername, int port) {

        if (!openSockets.containsKey(srcUsername)) {
            try {
                Socket socket = new Socket("localhost", port);
                readMessage(socket, true);
                openSockets.put(srcUsername, socket);
                this.listener.clientSocketAdded(srcUsername, socket);
                Message message = new Message(username, "unknown", String.valueOf(this.port));
                sendMessage(srcUsername, socket, message);
                // printOpenSockets();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String user, Socket socket, Message message) {

        ObjectOutputStream dout;
        try {
            if (doutStreams.containsKey(user)) {
                dout = doutStreams.get(user);
            } else {
                dout = new ObjectOutputStream(socket.getOutputStream());
                doutStreams.put(user, dout);
            }
            dout.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(ObjectOutputStream dout, Message message) {

        new Thread(new Runnable() {

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


    private void readMessage(Socket socket, boolean continous) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean connected = false;
                Message message;
                try {
                    ObjectInputStream din = new ObjectInputStream(socket.getInputStream());
                    do {
                        message = (Message) din.readObject();
                        System.out.println(port + " : " + message.getData());
                        if (!connected && message.getReceiver().equals("unknown")) {
                            connected = true;
                            openSockets.put(message.getSender(), socket);
                            listener.clientSocketAdded(message.getSender(), socket);
                            // printOpenSockets();
                        }else{
                            context.messageReceived(message);
                        }
                    } while (continous);
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
                        readMessage(socket, true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressWarnings("unused")
    private void printOpenSockets() {
        for (Map.Entry<String, Socket> mEntry : openSockets.entrySet()) {
            System.out.println(port + " : " + mEntry.getKey() + " : " + mEntry.getValue());
        }
    }

}