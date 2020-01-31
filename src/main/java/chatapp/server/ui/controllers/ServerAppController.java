package chatapp.server.ui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import chatapp.server.ClientListener;
import chatapp.server.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;



public class ServerAppController implements Initializable, ClientListener {

    @FXML
    Label label;
    @FXML
    Button btnStartStopServer;
    @FXML
    ListView<String> listActiveClients;
    @FXML TextField textPort;


    private Server server;
    private boolean serverStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server = new Server();
        serverStatus = false;
        textPort.appendText(String.valueOf(server.getPort()));
        server.attachClientListener(this);
    }

    public void serverHandler(MouseEvent event) {

        this.serverStatus = !this.serverStatus;

        if(this.serverStatus){
           btnStartStopServer.setText("Server Started.");
           btnStartStopServer.setDisable(true);
           new Thread( new Runnable(){
           
               @Override
               public void run() {
                   try{
                        server.listen();
                   }catch(IOException e){
                       e.printStackTrace();
                   } 
               }
           }).start();;
        }
        else{
            btnStartStopServer.setText("Start Server");
            //server.stop();
        }
    }


    @Override
    public void update(String id) {
        System.out.println(id);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listActiveClients.getItems().add(id);
            }
        });
        
    }


}