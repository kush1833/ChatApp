package chatapp.client.ui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import chatapp.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ClientAppController implements Initializable {

    @FXML Button btnStart;
    @FXML TextField textUsername;
    @FXML AnchorPane rootWindow;

    Client client;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        client = new Client("localhost", 3050);
    }

    public void startClientHandler(MouseEvent event) throws IOException {
        final String username = textUsername.getText();
        client.setUsername(username);
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/clientChat.fxml"));
        rootWindow.getChildren().setAll(pane);
    }
}