package chatapp.client.ui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import chatapp.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ClientAppController implements Initializable {

    @FXML AnchorPane rootWindow;

    // AnchorPane1
    @FXML AnchorPane anchorPane1;
    @FXML Button btnStart;
    @FXML TextField textUsername;
    
    // AnchorPane2
    @FXML AnchorPane anchorPane2;
    @FXML Button btnStartChat;
    @FXML TextField textUsernameToChat;
    
    
    private Client client;
    private List<String> chatUsernames;
    
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        client = new Client("localhost", 3050);
    }

    
    public void startClientHandler(MouseEvent event) {
        final String username = textUsername.getText();
        client.setUsername(username);

        client.start(); // This is blocking call till handshake
        System.out.println("Handshaked");
        anchorPane1.setVisible(false);
        anchorPane2.setVisible(true);   
        // AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/clientChat.fxml"));
        //rootWindow.getChildren().setAll(pane);
    }


    public void startChatHandler(MouseEvent event){

        String userString = textUsernameToChat.getText();
        StringTokenizer users = new StringTokenizer(userString);
        chatUsernames = new ArrayList<String>();
        while(users.hasMoreTokens()){
            chatUsernames.add(users.nextToken());
           
        }
         System.out.println(chatUsernames.toString());

    }

}