package chatapp.clientP2P;

import java.net.Socket;

public interface ClientConnectionListener {
    public void clientSocketAdded(String username,Socket socket);
}