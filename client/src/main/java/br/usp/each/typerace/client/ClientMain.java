package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ClientMain {

    private WebSocketClient client;
    private static final Scanner scanner = new Scanner(System.in);

    public ClientMain(WebSocketClient client) {
        this.client = client;
    }

    public void init(String idCliente) {
        System.out.println("init client: " + idCliente);
        client.connect();
    }

    private static int configPort(){
        System.out.println("We need tell us, what port you want connect, for default we use the port 8080");
        System.out.println();
        String in = scanner.nextLine();
        int temp = Integer.parseInt(in);
        if(0 < temp && temp < 65535) {
            System.out.println(temp);
            return temp;
        }
        System.out.println("Wrong port try again");
        return configPort();


    }

    private static String configID(){
        System.out.println("Now we need choice your ID (Without space, please), choice a nice name");
        String id = scanner.next();
        scanner.nextLine();
        System.out.println();
        if(id.isBlank() || id.equalsIgnoreCase("DanielCordeiro")){
            System.out.println("ARE U KIDDING ME?? TRY AGAIN, PLEASE");
            return configID();
        }else{
            if(id.equalsIgnoreCase("ActuallyAReallyNiceId"))
                System.out.println("OMG THAT'S ID IS VERY NICE");
            else if(id.equalsIgnoreCase("SPIDERMAN"))
                System.out.println("Your id is AMAZING");
            else
                System.out.println("You think that's nice\n... Okay, if you think\n... Let do it");
            return id;
        }
    }

    public static void main(String[] args) {
        /*
           FIXME: Remover essas strings fixas
           //POR QUE??? ELAS TEM SENTIMENTOS SABIA!!!

           Como podemos fazer para que o cliente receba um parâmetro indicando a qual servidor
           ele deve se conectar e o seu ID?
        */
        System.out.println("Welcome to INFINITE MONKEY's game");
        int port =  configPort();
        String id = configID();
        String portURL = "ws://localhost:"+ port +"/playerID="+id;

        try {
            WebSocketClient client = new Client(new URI(portURL));

            ClientMain main = new ClientMain(client);

            main.init(id);

            while(!client.isClosed()){
                String message =  scanner.nextLine();
                client.send(message);
                System.out.println(message);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
