package chatapp.client.ui.controllers;

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

public class ClientChatController implements Initializable {


    @FXML Label labelReceiverList;
    @FXML Button btnSendMessage;
    @FXML TextField textMessage;
    @FXML ListView<String> listView;

    private Set<String> receiversSet;
    private Client client;
    private String initData;
    private String userString;

    public void initReceiverSet(Set<String> receiversSet, String userString, String initData){
        this.receiversSet = receiversSet;
        this.initData = initData;
        this.userString = userString;
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
               listView.getItems().add(message.getData());
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
               listView.getItems().add(message.getData());
            }     
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(initData);
        System.out.println(userString);
        this.client = ClientAppController.getClient();
       
    }
}