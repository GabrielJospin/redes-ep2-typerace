package br.usp.each.typerace.server;

import org.java_websocket.server.WebSocketServer;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class is responsible to up the Server in the port of settings.json
 *
 * */
public class ServerMain {

    private WebSocketServer server;
    private static final String PATH_TO_SETTINGS = "settings.json";

    /**
     * @param server: The Server to UP
     * */
    public ServerMain(WebSocketServer server) {
        this.server = server;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    /**
     * init the server
     * */
    public void init() {
        LOGGER.info("INIT SERVER...");
        server.start();
    }

    /**
     * get the json from settings.json
     *
     * @return JSONObject json
     * */
    public static JSONObject getJson(){
        File file = new File(PATH_TO_SETTINGS);
        String content = "";

        try {
            content = FileUtils.readFileToString(file,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject(content);
    }

    /**
     * test if the port is valid
     *
     * @param port: the port to use
     * @return the port or 8080 if the port is invalid
     * */
    public static int testPort(int port){
        if(port < 1 || port > 65535){
            LOGGER.error("INVALID PORT!!\nused 8080 as port, sorry man");
            return 8080;
        }
        return port;
    }

    /**
     * run the server
     * */
    public static void main(String[] args) {

        JSONObject json = getJson();
        int port = json.getInt("port");
        port = testPort(port);


        WebSocketServer server = new Server(port, new HashMap<>());

        ServerMain main = new ServerMain(server);

        main.init();

        LOGGER.info("Server started on por: "+port);
    }

}
