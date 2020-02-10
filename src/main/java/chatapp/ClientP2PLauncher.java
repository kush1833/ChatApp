package chatapp;

import java.net.Socket;


import chatapp.clientP2P.OnCompleteListener;
import chatapp.clientP2P.Peer;


public class ClientP2PLauncher {

    public static void main(String[] args) {

        Peer p1 = new Peer("div", 3000);
        p1.addPeer(3001);
        p1.listen();

        Peer p2 = new Peer("adi", 3001);
        p2.addPeer(3002);
        p2.listen();

        Peer p3 = new Peer("kush", 3002);
        p3.listen();

        
        p1.connectUser("kush").addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(Socket socket) {
               p1.sendMessage("kush", "test-data");
               p1.sendMessage("kush","data-2");
            }
        });


        
    }
}