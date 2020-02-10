package chatapp.client.ui.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import chatapp.client.Client;
import chatapp.message.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ClientChatController implements Initializable {


    @FXML Label labelReceiverList;
    @FXML Button btnSendMessage;
    @FXML Button btnSendFile;
    @FXML TextField textMessage;
    @FXML ListView<String> listView;

    private Set<String> receiversSet;
    private Client client;

    public void initReceiverSet(Set<String> receiversSet, String userString, String initData){
        this.receiversSet = receiversSet;
         if(initData != null)
            listView.getItems().add(initData);
        labelReceiverList.setText(userString);
    }

    public Set<String> getReceiversList(){
        return this.receiversSet;
    }

    public void updateMessage(Message message){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               String msg = message.getSender()+" : "+message.getData();
               listView.getItems().add(msg);
            }     
        });
    }

    public void sendMessageHandler(MouseEvent event){
        String msg = textMessage.getText().trim();
        Message message = new Message(client.getUsername(), this.receiversSet,msg);
        client.sendMessage(message);
      
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               textMessage.setText("");
               String msg = client.getUsername()+" : "+message.getData();
               listView.getItems().add(msg);
            }     
        });
    }

    public void sendFileHandler(MouseEvent event){
        
        Stage stage = new Stage();
        stage.setTitle("File Chooser");
        FileChooser fileChooser = new FileChooser();
        stage.show();
        File file = fileChooser.showOpenDialog(stage); 
        System.out.println(file);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.client = ClientAppController.getClient();
       
    }
}