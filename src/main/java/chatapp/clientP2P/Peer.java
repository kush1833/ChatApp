package chatapp.clientP2P;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import chatapp.message.PeerFindPacket;

public class Peer {

    private int port;
    private Set<Integer> peersSet;
    private String username;

    private InetAddress inetAddress;

    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    private SocketHandler socketHandler;


    public Peer(String username, int port) {

        this.username = username;
        this.port = port;
        try {
            datagramSocket = new DatagramSocket(this.port);
            datagramPacket = new DatagramPacket(new byte[1024], 1024);
            inetAddress = InetAddress.getByName("localhost");
        } catch (SocketException soe) {
            System.out.println("Error: Starting DATAGRAM_SOCKET");
        } catch (UnknownHostException uhe) {
            System.out.println("Error : Unknown Host");
        }
        peersSet = new HashSet<>();
        socketHandler = new SocketHandler(port);
    }

    public void addPeer(int peerPort) {
        peersSet.add(peerPort);
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
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }


    private void processPeerFindData(byte[] packetBytes) {

        ByteArrayInputStream bais;
        ObjectInputStream ois;
        PeerFindPacket packet;

        bais = new ByteArrayInputStream(packetBytes);
        try {
            ois = new ObjectInputStream(bais);
            packet = (PeerFindPacket) ois.readObject();
            if (packet.getDestinationUsername().equals(username)) {

                socketHandler.connectSocket(packet.getSourcePort());
            
            }else {
                sendPeerFindPacket(packet);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void sendPeerFindPacket(PeerFindPacket packet) {

        ByteArrayOutputStream baos;
        ObjectOutputStream oos;

        int sourcePort = packet.getSourcePort();
        packet.hopped();

        baos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] packetBytes = baos.toByteArray();
        // System.out.println("Size : " + packetBytes.length);
        new Thread(new Runnable() {
            @Override
            public void run() {

                DatagramPacket datagramPacket;
                for (int port : peersSet) {

                    if (port != sourcePort) {
                        datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, inetAddress, port);
                        try {
                            datagramSocket.send(datagramPacket);
                        } catch (IOException e) {
                            System.out.println("Error : Sending to peer failed : " + port);
                        }
                    }
                }
            }
        }).start();
    } 
}



// public interface UserOnConnectListener {
//         public void onComplete();
//     }

//     public static class TaskUserListener {

//         private String descUsername;
//         private UserOnConnectListener listener;
//         private static Map<String, TaskUserListener> activeTasks = new HashMap<>();

//         TaskUserListener(String username) {
//             descUsername = username;
//             activeTasks.put(descUsername, this);
//         }

//         public void addOnConnectListener(UserOnConnectListener listener) {
//             this.listener = listener;
//         }

//         public void update(int port) {
//             this.listener.onComplete();
//         }
//     }
