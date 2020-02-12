package chatapp.clientP2P;

public interface MessageReceiveListener {
    public void onMessageReceived(String message, String sender);
}