package chatapp.clientP2P;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import chatapp.message.Message;
import chatapp.message.PeerFindPacket;

public class Peer implements ClientConnectionListener {

    private final int port;
    private final Set<Integer> peersSet;
    private final String username;

    private InetAddress inetAddress;

    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    protected Map<String, Socket> openSockets;
    private final SocketHandler socketHandler;



    public Peer(final String username, final int port) {

        this.username = username;
        this.port = port;
        try {
            datagramSocket = new DatagramSocket(this.port);
            datagramPacket = new DatagramPacket(new byte[1024], 1024);
            inetAddress = InetAddress.getByName("localhost");
        } catch (final SocketException soe) {
            System.out.println("Error: Starting DATAGRAM_SOCKET");
        } catch (final UnknownHostException uhe) {
            System.out.println("Error : Unknown Host");
        }
        peersSet = new HashSet<>();
        socketHandler = new SocketHandler(port);
        socketHandler.addClientConnectionListener(this);
        openSockets = new HashMap<>();
    }



    public void addPeer(final int peerPort) {
        peersSet.add(peerPort);
    }



    @Override
    public void clientSocketAdded(final String username, final Socket socket) {
        this.openSockets.put(username, socket);
        printOpenSockets();
        TaskUserListener.clientSocketAdded(username, socket);
    }
    

    private void printOpenSockets(){
        for(Map.Entry<String,Socket> mEntry : openSockets.entrySet()){
            System.out.println(port+" : "+mEntry.getKey()+" : "+mEntry.getValue());
        }
    }


    public void sendMessage(String descUsername, String msg){

        Message message = new Message(username, descUsername, msg);
        Socket s = openSockets.get(descUsername);
        socketHandler.sendMessage(descUsername, s, message);

    }


    public void listen() {


        socketHandler.startServer();

        new Thread(new Runnable() {

            @Override
            public void run() {

                System.out.println("Starting Peer : " + port);
                while (true) {
                    try {
                        datagramSocket.receive(datagramPacket);
                        processPeerFindData(datagramPacket.getData());
                    } catch (final IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }



    private void processPeerFindData(final byte[] packetBytes) {

        ByteArrayInputStream bais;
        ObjectInputStream ois;
        PeerFindPacket packet;

        bais = new ByteArrayInputStream(packetBytes);
        try {
            ois = new ObjectInputStream(bais);
            packet = (PeerFindPacket) ois.readObject();
            if (packet.getDestinationUsername().equals(username)) {

                socketHandler.connectSocket(username, packet.getSourceUsername(), packet.getSourcePort());

            } else {
                sendPeerFindPacket(packet);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void sendPeerFindPacket(final PeerFindPacket packet) {

        ByteArrayOutputStream baos;
        ObjectOutputStream oos;

        final int sourcePort = packet.getSourcePort();
        packet.hopped();

        baos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(packet);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final byte[] packetBytes = baos.toByteArray();
        // System.out.println("Size : " + packetBytes.length);
        new Thread(new Runnable() {
            @Override
            public void run() {

                DatagramPacket datagramPacket;
                for (final int port : peersSet) {

                    if (port != sourcePort) {
                        datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, inetAddress, port);
                        try {
                            datagramSocket.send(datagramPacket);
                        } catch (final IOException e) {
                            System.out.println("Error : Sending to peer failed : " + port);
                        }
                    }
                }
            }
        }).start();
    }



    public TaskUserListener connectUser(String descUsername) {

        System.out.println(descUsername+" : "+openSockets.containsKey(descUsername));
        if(openSockets.containsKey(descUsername))
            return new TaskUserListener(openSockets.get(descUsername));
        PeerFindPacket packet = new PeerFindPacket(this.port,this.username, descUsername);
        sendPeerFindPacket(packet);
        return new TaskUserListener(descUsername);
    }
    
    public static class TaskUserListener {

        private OnCompleteListener listener;
        private static Map<String, TaskUserListener> activeTasks = new HashMap<>();
        private boolean alreadyPresent;
        private Socket socket;

        public TaskUserListener(String descUsername) {
            activeTasks.put(descUsername, this);
            this.socket = null;
        }

        public TaskUserListener(Socket socket) {
            alreadyPresent = true;
            this.socket = socket;
        }

        public void addOnCompleteListener(OnCompleteListener listener){
            this.listener = listener;
            if(alreadyPresent)
                this.listener.onComplete(this.socket);
        }

        public static void clientSocketAdded(String username, Socket socket){
         
            if(activeTasks.containsKey(username)){
                activeTasks.get(username).listener.onComplete(socket);
                activeTasks.remove(username);
            }
        }

    }

}
