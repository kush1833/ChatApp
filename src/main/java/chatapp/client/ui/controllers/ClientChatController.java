package chatapp.client.ui.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import chatapp.message.Message;
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


    private List<String> receiversList;

    public void initReceiverList(List<String> receiverList, String list){
        this.receiversList = receiverList;
        labelReceiverList.setText(list);
    }

    public List<String> getReceiversList(){
        return this.receiversList;
    }

    public void updateMessage(Message message){

    }

    public void sendMessageHandler(MouseEvent event){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}