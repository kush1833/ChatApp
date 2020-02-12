package chatapp.message;

import java.io.Serializable;

public class PeerFindPacket implements Serializable {

    private static final long serialVersionUID = 1L;

    private int sourcePort;
    private String descUsername;
    private String srcUsername;
    private int hops;

    public PeerFindPacket(int sourcePort, String srcUsername, String desUsername) {
        this.sourcePort = sourcePort;
        this.descUsername = desUsername;
        this.srcUsername = srcUsername;
        this.hops = 4;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public String getDestinationUsername() {
        return this.descUsername;
    }

    public String getSourceUsername() {
        return this.srcUsername;
    }

    public int getHops() {
        return this.hops;
    }

    public void hopped() {
        if (this.hops > 0)
            this.hops--;
    }
}