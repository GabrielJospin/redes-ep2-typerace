package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class Client extends WebSocketClient {

    boolean isClose;

    public Client(URI serverUri) {
        super(serverUri);
        this.isClose = false;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(" successfully connected");

    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        this.isClose = true;
        System.out.println("Closed");
        System.out.println("code: "+code);
        System.out.println(reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("A ERROR OCCURRED");
        System.out.println(ex.getMessage());
    }
}
