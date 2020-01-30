package chatapp.UI.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import chatapp.Server.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class AppController implements Initializable {

    @FXML
    Label label;
    @FXML
    Button btnStartServer;

    private Server server;

    public void startServer(MouseEvent event) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    server.listen();
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }).start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server = new Server();
    }

}