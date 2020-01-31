package chatapp.client;

import chatapp.message.Message;

public interface ReceiveMessageListener{
    public void onComplete(Message message);
}