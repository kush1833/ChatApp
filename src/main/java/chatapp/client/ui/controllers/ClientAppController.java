package chatapp.client.ui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import chatapp.client.Client;
import chatapp.client.ReceiveMessageListener;
import chatapp.message.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientAppController implements Initializable, ReceiveMessageListener {

    @FXML
    AnchorPane rootWindow;

    // AnchorPane1
    @FXML
    AnchorPane anchorPane1;
    @FXML
    Button btnStart;
    @FXML
    TextField textUsername;

    // AnchorPane2
    @FXML
    AnchorPane anchorPane2;
    @FXML
    Button btnStartChat;
    @FXML
    TextField textUsernameToChat;

    private Client client;
    private Map<String, ClientChatController> controllerMap;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        client = new Client("localhost", 3050);
        client.attachReceiveMessageListener(this);
        controllerMap = new HashMap<>();
    }

    public void startClientHandler(final MouseEvent event) {
        final String username = textUsername.getText();
        client.setUsername(username);
        client.start();
    }

    public void startChatHandler(final MouseEvent event) {

        final String userString = textUsernameToChat.getText();
        final StringTokenizer users = new StringTokenizer(userString);
        final List<String> chatUsernames = new ArrayList<String>();
        while (users.hasMoreTokens()) {
            chatUsernames.add(users.nextToken());
        }
        loadNewChatWindow(null, chatUsernames, userString, null);
        textUsernameToChat.setText("");
    }



    private void loadNewChatWindow(String chatId, List<String> chatUsernames, String userString, String m){

        if(chatId == null)
            chatId = String.valueOf(System.currentTimeMillis());

        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clientChat.fxml"));
            final Parent root = (Parent) loader.load();
            final ClientChatController controller = loader.getController();
            controller.initReceiverList(client, chatId, chatUsernames, userString, m);
            controllerMap.put(chatId, controller);
            final Stage stage = new Stage();
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onComplete(final Message message) {

        System.out.println(controllerMap.toString());


        // Check for Hello Message
        if (message.getSender().equals("server") && message.getData().equals("hello")) {
            System.out.println("Handshake Done");
            anchorPane1.setVisible(false);
            anchorPane2.setVisible(true);
        } else {

            System.out.println(message.toString());
            String chatId = message.getChatId();

            if (controllerMap.containsKey(chatId)) {
                controllerMap.get(chatId).updateMessage(message);
            } else {
                
                List<String> chatUsernames = message.getReceivers();
                chatUsernames.remove(client.getUsername());
                chatUsernames.add(message.getSender());
                String userString = "";
                for(String s: chatUsernames){
                    userString+=s;
                }
                System.out.println(userString);
                loadNewChatWindow(chatId, chatUsernames, userString, message.getData());
            }
        }
    }

}