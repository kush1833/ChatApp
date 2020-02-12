package chatapp.clientP2P.ui.controllers;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import chatapp.clientP2P.MessageReceiveListener;
import chatapp.clientP2P.OnCompleteListener;
import chatapp.clientP2P.Peer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ClientP2PAppController implements Initializable, MessageReceiveListener {

    @FXML
    TextField textMessage;
    @FXML
    TextField textPeers;
    @FXML
    TextField textPort;
    @FXML
    TextField textUsername;
    @FXML
    TextField textDescUsername;
    @FXML
    Button btnSend;
    @FXML
    Button btnStart;
    @FXML
    Button btnConnect;
    @FXML
    ListView<String> listView;

    private Peer peer;
    private String username;
    private String descUsername;
    private boolean connected;

    public void sendMessageHandler(MouseEvent event) {

        String msg = textMessage.getText().trim();
        peer.sendMessage(descUsername, msg);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listView.getItems().add(msg);
            }
        });
    }

    public void connectUserHandler(MouseEvent event) {
        descUsername = textDescUsername.getText().trim();
        peer.connectUser(descUsername).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Socket socket) {
                enableChat();
            }
        });
    }

    public void startPeerHandler(MouseEvent event) {
        int port = Integer.parseInt(textPort.getText().trim());
        username = textUsername.getText().trim();
        peer = new Peer(username, port);
        peer.addMessageReceiveListener(this);
        peer.listen();

        String peersStr = textPeers.getText().trim();
        String[] peers = peersStr.split(" ");
        for (int i = 0; i < peers.length; i++) {
            peer.addPeer(Integer.parseInt(peers[i]));
        }
        textUsername.setDisable(true);
        textPort.setDisable(true);

        btnConnect.setDisable(false);
        textDescUsername.setDisable(false);
    }

    private void disableChat() {
        listView.setVisible(false);
        btnConnect.setDisable(true);
        textDescUsername.setDisable(true);
    }

    private void enableChat() {
        listView.setVisible(true);
        btnSend.setDisable(false);
        textMessage.setDisable(false);
        btnConnect.setDisable(true);
        textDescUsername.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableChat();
        btnSend.setDisable(true);
        textMessage.setDisable(true);
        connected = false;
    }

    @Override
    public void onMessageReceived(String message, String sender) {

        System.out.println(message);
        if(!connected){
            connected = true;
            descUsername = sender;
            textDescUsername.setText(sender);
            enableChat();
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listView.getItems().add(message);
            }
        });
    }

}