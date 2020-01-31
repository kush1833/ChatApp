package chatapp;

import chatapp.server.ui.App;

public class MainServer {

    public static void main(String args[]) {
        App app = new App();
        app.render(args);
    }

}