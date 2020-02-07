package chatapp.client.ui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import com.google.common.collect.Sets;

import chatapp.client.Client;
import chatapp.client.ReceiveMessageListener;
import chatapp.message.Message;
import javafx.application.Platform;
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

    private static Client client;
    private Map<ClientChatController, Set<String>> controllerMap;

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
        final Set<String> chatUsernames = new HashSet<String>();
        while (users.hasMoreTokens()) {
            chatUsernames.add(users.nextToken());
        }
        textUsernameToChat.setText("");
        loadNewChatWindow(chatUsernames, userString, null);
    }

    private void loadNewChatWindow(Set<String> receiversSet, String userString, Message message) {

        ClientChatController ccc = chatWindowExists(receiversSet);

        if (ccc == null) {

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    String initData = null;
                    if (message != null)
                        initData = message.getData();

                    try {
                        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clientChat.fxml"));
                        final Parent root = (Parent) loader.load();
                        final ClientChatController controller = loader.getController();
                        controller.initReceiverSet(receiversSet, userString, initData);
                        controllerMap.put(controller, receiversSet);
                        final Stage stage = new Stage();
                        final Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (final IOException e) {
                        System.out.println(e.getMessage());
                    }

                }
            });
        } else {
            ccc.updateMessage(message);
        }
    }

    public static Client getClient() {
        return client;
    }

    private ClientChatController chatWindowExists(Set<String> receiversSet) {

        Set<String> existingSet;

        for (Map.Entry<ClientChatController, Set<String>> mapEl : controllerMap.entrySet()) {
            existingSet = (Set<String>) mapEl.getValue();
            if (receiversSet.equals(existingSet)) {
                return mapEl.getKey();
            }
        }
        return null;
    }

    @Override
    public void onComplete(final Message message) {

        // Check for Hello Message
        if (message.getSender().equals("server") && message.getData().equals("hello")) {
            System.out.println("Handshake Done");
            anchorPane1.setVisible(false);
            anchorPane2.setVisible(true);
        } else {

            Set<String> chatUsernames = message.getReceivers();
            chatUsernames.remove(client.getUsername());
            chatUsernames.add(message.getSender());
            String userString = "";
            for (String s : chatUsernames) {
                userString = userString + s + " ";
            }
            System.out.println(userString);
            loadNewChatWindow(chatUsernames, userString, message);
        }
    }
}