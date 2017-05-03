package client;

import protocole.Request;
import server.MyServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 4 stages of a client's life :
 *  1. Socket's instantiation;
 *  2. Connect the socket with the service server's side targeted;
 *  3. Exchange of data with server's sockets;
 *  4. Close the socket when the communications are done.
 */
public class MyClient {

    private int port;
    private String host;

    public MyClient(int port, String host){
        this.port = port;
        this.host = host;
    }

    public void start(){
        try {
            //Step 1;
            Socket socket = new Socket(host, port);

            //Step 2;
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Step 3;
            while(true){
                Request request;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
