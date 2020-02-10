package chatapp.clientP2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketHandler {

    private ServerSocket serverSocket;
    private Socket socket;

    private int port;

    private Map<Integer, Socket> openSockets;

    public SocketHandler(int port) {
        this.port = port;
        this.openSockets = new HashMap<>();
    }

    private void addSocket(Socket socket) {

        if (!openSockets.containsKey(socket.getPort())) {
            readMessage(socket);
            openSockets.put(socket.getPort(), socket);

        }
    }

    public void connectSocket(int port) {

        if (!openSockets.containsKey(port)) {
            try {
                Socket socket = new Socket("localhost", port);
                System.out.println(this.port+" : connectSocket : " + socket.getPort() + " : " + socket.getLocalPort());
                readMessage(socket);
                openSockets.put(port, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessage(Socket socket) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean connected = true;

                try {
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    while (connected) {
                        String message = din.readUTF();
                        System.out.println(socket.getPort() + " : " + message);
                    }
                } catch (IOException e) {
                    connected = false;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(int port, String message) {
        Socket socket = openSockets.get(port);
        try {
            new DataOutputStream(socket.getOutputStream()).writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    serverSocket = new ServerSocket(port);
                    while (true) {
                        socket = serverSocket.accept();
                        System.out.println(port+" : startServer : " + socket.getPort() + ": " + socket.getLocalPort());
                        addSocket(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}