package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

/**
 * class to config the client extend {@link org.java_websocket.handshake.ServerHandshake}
 * */
public class Client extends WebSocketClient {

    boolean isClose;
    /**
     * Constructs a WebSocketClient instance and sets it to the connect to the specified URI. The
     * channel does not attampt to connect automatically. The connection will be established once you
     * call <var>connect</var>.
     * @param serverUri: the URI to up the client
     * */
    public Client(URI serverUri) {
        super(serverUri);
        this.isClose = false;
    }

    /**
     * Called after an opening handshake has been performed and the given websocket is ready to be
     * written on.
     *
     * @param handshakedata The handshake of the websocket instance
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(" successfully connected");

    }

    /**
     * Callback for string messages received from the remote host
     *
     * @param message The UTF-8 decoded message that was received.
     * @see #onMessage(ByteBuffer)
     **/
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    /**
     * Called after the websocket connection has been closed.
     *
     * @param code   The codes can be looked up here: {@link CloseFrame}
     * @param reason Additional information string
     * @param remote Returns whether or not the closing of the connection was initiated by the remote
     *               host.
     **/
    @Override
    public void onClose(int code, String reason, boolean remote) {
        this.isClose = true;
        System.out.println("Closed");
        System.out.println("code: "+code);
        System.out.println(reason);
    }

    /**
     * Called when errors occurs. If an error causes the websocket connection to fail {@link
     * #onClose(int, String, boolean)} will be called additionally.<br> This method will be called
     * primarily because of IO or protocol errors.<br> If the given exception is an RuntimeException
     * that probably means that you encountered a bug.<br>
     *
     * @param ex The exception causing this error
     **/
    @Override
    public void onError(Exception ex) {
        System.out.println("A ERROR OCCURRED");
        System.out.println(ex.getMessage());
    }
}
