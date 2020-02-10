package chatapp;

import chatapp.clientP2P.Peer;
import chatapp.message.PeerFindPacket;

public class ClientP2PLauncher {

    public static void main(String[] args) {

        Peer p1 = new Peer("div",3000);
        p1.addPeer(3001);
        p1.listen();


        Peer p2 =  new Peer("adi",3001);
        p2.addPeer(3002);
        p2.listen();

        Peer p3 = new Peer("kush",3002);
        p3.listen();


        PeerFindPacket packet = new PeerFindPacket(3000,"div", "kush");
        PeerFindPacket packet1 = new PeerFindPacket(3000,"div" ,"adi");
        
        p1.sendPeerFindPacket(packet);
        p1.sendPeerFindPacket(packet1);
        
    }
}