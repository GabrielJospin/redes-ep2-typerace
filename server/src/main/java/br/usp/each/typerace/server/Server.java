package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Map;

public class Server extends WebSocketServer {

    private final Map<String, WebSocket> connections;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        LOGGER.info("New Connection");
        if(testId(conn)){
            LOGGER.info("An Valid Connection");
            String id = getId(conn);
            this.connections.put(id,conn);
            int numConn = getNumConnections();
            String message = "";
            if(numConn % 2 == 1) {
                message = String.format("\n\n-%s, You are late\n" +
                                "-A Wizard is never late. Frodo Baggins\n" +
                                "Nor is he early. he arrives precisely when he means to\n" +
                                "(the monkey, %s, just jumped in the game)\n" +
                                "Now, the Funniest Lobby of this game has %d participants\n\n",
                        id, id, numConn);
            }else {
                message = String.format("\n\n-HELLO THERE!!\n" +
                                "-General, %s!!\n" +
                                "(the monkey, %s, just jumped in the game)\n" +
                                "Now, the Funniest Lobby of this game has %d participants\n\n",
                        id, id, numConn);
            }
            broadcast(message);
            message = "\nWELCOME TO THE INFINITE MONKEY'S GAME\n\n" +
                    "History:\n" +
                    "The Infinite Monkey is a terrible and strong entity, He capture terrestrial primates to write words\n" +
                    "for infinite time, your the most words right possible to prove the Infinite Monkey Theorem\n" +
                    "Good Lucky, my little Possani's Padawan\n" +
                    "May The Force Be With You\n\n" +
                    "Rules:\n" +
                    "\tSend one word at a time (Just Breathe, Madeline)\n" +
                    "\tDon't send the words out of order (I'll not receive for code this, so please be kind)\n" +
                    "\tThe words can be uppercase, lowercase (camelcase, i really don't care about it.)\n" +
                    "\t Never, ever eat the cake\n" +
                    "\tHave fun!! Because i've so much coding it!!!\n\n";
            conn.send(message);
        }

    }



    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String id =getId(conn);
        broadcast(String.format("The %s monkey is out of server, goodbye little monkey\n", id));
        LOGGER.info(String.format("Lost connection: %s (%s)\n",
                getId(conn), conn.getRemoteSocketAddress().getAddress().getHostAddress()));
        connections.remove(id);
        if(!conn.isClosed())
            conn.close(code, reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // TODO: Implementar
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //if(ex instanceOf ????) Possivel futuro pra aplicação
        if(conn != null){
            LOGGER.error(String.format("Error on connection %s\n", getId(conn)), ex);
            conn.send("an error detected, please wait, and eat a piece of cake\n");
        }else{
            LOGGER.error("Error on application\n", ex);
            broadcast("An error detected, please wait, and eat a piece of cake\n");
        }

    }

    @Override
    public void onStart() {
        LOGGER.info("Server started\n");
    }

    private String getId(WebSocket conn){
        String rd = conn.getResourceDescriptor();
        return rd.substring(rd.indexOf("player id=")+ 9);
    }

    private boolean testId(WebSocket conn){
        if(connections.containsKey(getId(conn))){
            conn.send("ID ALREADY EXIST!\nPlease, try again with another id\n");
            conn.close(400, "invalidName");
            return false;
        }
        return true;
    }

    private int getNumConnections() {
        return connections.size();
    }
}
