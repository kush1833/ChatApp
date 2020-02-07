package chatapp.client.ui.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    private String chatId;
    private List<String> receiversList;
    private Client client;

    public void initReceiverList(Client client, String chatId, List<String> receiverList, String list, String dm){
        this.chatId = chatId;
        this.client = client;
        this.receiversList = receiverList;
        labelReceiverList.setText(list);
        if(dm != null){
            listView.getItems().add(dm);
        }
    }

    public List<String> getReceiversList(){
        return this.receiversList;
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
        Message message = new Message(chatId,client.getUsername(), this.receiversList,msg);
        client.sendMessage(message);
        textMessage.setText("");
        listView.getItems().add(msg);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}