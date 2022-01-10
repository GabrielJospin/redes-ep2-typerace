package br.usp.each.typerace.server;

import com.opencsv.exceptions.CsvException;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
/**
 * Class to config the server extending {@link org.java_websocket.server.WebSocketServer}
 * */
public class Server extends WebSocketServer {

    private final Map<String, WebSocket> connections;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private final Game game;

    /**
     * @param port: The port to up the connection
     * @param connections: The connections with the sting of respective ID
     * */
    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
        this.game = new Game();
    }

    /**
     * Called after an opening handshake has been performed and the given websocket is ready to be
     * written on.
     *
     * @param conn      The <tt>WebSocket</tt> instance this event is occurring on.
     * @param handshake The handshake of the websocket instance
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {


        LOGGER.info("New Connection");
        if(testId(conn)){

            LOGGER.info("An Valid Connection");
            String id = getId(conn);
            if(!game.addPlayer(getId(conn))){
                conn.send("The players number is above the limit, sorry, try later");
                conn.close(500, "A lot of players connected");
                return;
            }

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
            sendToAll(message);
            message = "\nWELCOME TO THE INFINITE MONKEY'S GAME\n\n" +
                    "History:\n" +
                    "The Infinite Monkey is a terrible and strong entity, He capture terrestrial primates to write words\n" +
                    "for infinite time, your the most words right possible to prove the Infinite Monkey Theorem\n" +
                    "Good Lucky, my little Possani's Padawan\n" +
                    "May The Force Be With You\n\n" +
                    "Rules:\n" +
                    "\t- Send one word at a time (Just Breathe, Madeline)\n" +
                    "\t- You can send the words out of order (DON'T PANIC!! Yoda can play this game!)\n" +
                    "\t- The words can be uppercase, lowercase (camelcase, i really don't care about it.)\n" +
                    "\t- Never, ever eat the cake\n" +
                    "\t- Have fun!! Because i've so much coding it!!!\n" +
                    "To init write \"init\", and to out write \"out\" \n\n";
            conn.send(message);
        }else{
            conn.send("Invalid id, try again");
            conn.close(400, "Invalid id");
        }

    }



    /**
     * Called after the websocket connection has been closed.
     *
     * @param conn   The <tt>WebSocket</tt> instance this event is occurring on.
     * @param code   The codes can be looked up here: {@link CloseFrame}
     * @param reason Additional information string
     * @param remote Returns whether or not the closing of the connection was initiated by the remote
     *               host.
     **/
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String id =getId(conn);
        LOGGER.info(String.format("Lost connection: %s \n", getId(conn)));
        connections.remove(id);
        game.removePlayer(id);
        if(!conn.isClosed())
            conn.close(code, reason);
        sendToAll(String.format("The %s monkey is out of server, goodbye little monkey\n", id));
    }

    /**
     * Callback for string messages received from the remote host
     *
     * @param conn    The <tt>WebSocket</tt> instance this event is occurring on.
     * @param message The UTF-8 decoded message that was received.
     * @see #onMessage(WebSocket, ByteBuffer)
     **/
    @Override
    public void onMessage(WebSocket conn, String message) {
        if(game.getStatus().equals(Game.Status.START)){
            if(message.equalsIgnoreCase("Init")){
                sendToAll("STARTED GAME!!");
                sendToAll("YOUR OBJECTIVE IS WRITE THE WORDS OF THIS LIST, IF YOU CAN, OF COURSE:");
                sendToAll(game.getWords().toString());
                game.setStatus(Game.Status.RUNNING);
            }else if(message.equalsIgnoreCase("Out")){
                conn.close();
            }else{
                conn.send("Message Error");
            }
        }else{

            String id = getId(conn);
            if(game.checkAnswer(id, message)){
                conn.send("right Answer!!");
            }else{
                conn.send("Wrong Answer!!");
            }
            game.updateStatus();
            if(game.isGameFinished()){
                sendToAll("Hello Guys, The Gane is end, let's see the result???");
                List<Player> players = game.getPlayers();
                sendToAll("|\trank|\tname|\tCorrect|\tWrong|");
                for(int i =0; i< players.size(); i++){
                    Player player = players.get(i);
                    String layer = String.format("|\t%d|\t%s|\t%d|\t%d|",
                            i+1, player.getPlayerId(), player.getCorrect(), player.getWrong());
                    sendToAll(layer);
                }
                Player winner = players.get(0);
                message = String.format("Congratulation %s you are the winner!!", winner.getPlayerId());
                sendToAll(message);
                quitAll();
            }else{
                message = game.getWords().toString();
                conn.send("That's the words remaining: ");
                conn.send(message);
            }

        }
    }
    /**
     * Quit all connections on this server
     * */
    private void quitAll() {
        this.connections.forEach((id, conn) ->{
            conn.close();
            LOGGER.info(id+"closed");
        });

    }


    /**
     * Called when errors occurs. If an error causes the websocket connection to fail {@link
     * #onClose(WebSocket, int, String, boolean)} will be called additionally.<br> This method will be
     * called primarily because of IO or protocol errors.<br> If the given exception is an
     * RuntimeException that probably means that you encountered a bug.<br>
     *
     * @param conn Can be null if there error does not belong to one specific websocket. For example
     *             if the servers port could not be bound.
     * @param ex   The exception causing this error
     **/
    @Override
    public void onError(WebSocket conn, Exception ex) {
        //if(ex instanceOf ????) Possivel futuro pra aplicação
        if(conn != null){
            LOGGER.error(String.format("Error on connection %s\n", getId(conn)), ex);
            conn.send("an error detected, please wait, and eat a piece of cake\n");
        }else{

            LOGGER.error("Error on application\n"+ex.getLocalizedMessage()+" " , ex);
            sendToAll("An error detected, please wait, and eat a piece of cake\n");
        }

    }
    /**
     * Called when the server started up successfully.
     * <p>
     * If any error occurred, onError is called instead.
     */
    @Override
    public void onStart() {
        LOGGER.info("Server started\n");
        String path = "./src/main/resources/DB/LordOfRings.csv";
        try {
            game.init(path);
        } catch (IOException e) {
            LOGGER.error("Error of IO, problaby the path is wrong: "+path+"\n"+e.getMessage()+"\n");
        } catch (CsvException e) {
            LOGGER.error("Error of CSV \n"+e.getMessage());
        }
    }

    /**
     * get the id from connection
     * @param conn: The connection
     * @return String, the id
     * */
    private String getId(WebSocket conn){
        String rd = conn.getResourceDescriptor();
        return rd.substring(rd.indexOf("playerId=")+ 11);
    }

    /**
     * test the id if id isn't already insert
     * @param conn: The connection
     * @return boolean, if is valid
     * */
    private boolean testId(WebSocket conn){
        if(connections.containsKey(getId(conn))){
            conn.send("ID ALREADY EXIST!\nPlease, try again with another id\n");
            conn.close(400, "invalidName");
            return false;
        }
        return true;
    }

    /**
     * return the number of players connected
     * @return int, numConnections
     * */
    private int getNumConnections() {
        return connections.size();
    }

    /**
     * send a message to all connections
     * @param message: the message to be sent
     * */
    private void sendToAll(String message){

        this.connections.forEach((id, conn) ->{
            conn.send(message);
        });


    }
}
