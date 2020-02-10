package chatapp.clientP2P;

import java.net.Socket;

public interface OnCompleteListener {
    public void onComplete(Socket socket);
}