package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * 4 stages of a client's life :
 * 1. Socket's instantiation;
 * 2. Connect the socket with the service server's side targeted;
 * 3. Exchange of data with server's sockets;
 * 4. Close the socket when the communications are done.
 */

public class ClientTPNote {

    private int port;
    private String host;
    private List<String> ideas = new ArrayList<>();
    private static int id = 0;

    public ClientTPNote(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() {
        try {
            //Step 1;
            Socket socket = new Socket(host, port);

            //Step 2;
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.print(requestCreator());
            out.flush();
            System.out.println(listen(in));

            socket.close();
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }

    }

    public String requestCreator() {
        String msg = "Write :\n"
                + "\t\"PROPOSE\", if you want to create an idea;\n"
                + "\t\"LISTIDEAS\", if you want to display all the ideas;\n"
                + "\t\"#END\" to end any request.";

        Scanner sc = new Scanner(System.in);
        System.out.println(msg);

        try {
            String decision = sc.nextLine();
            switch (decision) {
                case "PROPOSE":
                    StringBuilder propose = new StringBuilder();
                    propose.append(decision + "\n");
                    System.out.println("What's your name?");
                    propose.append(sc.nextLine() + "\n");
                    System.out.println("What's the name of your idea?");
                    propose.append(sc.nextLine() + "\n");
                    System.out.println("What's your email address?");
                    propose.append(sc.nextLine() + "\n");
                    System.out.println("What technologies would be involved in the project?\n" +
                            " Format : tech1, tech2, tech3 ...");
                    propose.append(sc.nextLine() + "\n");
                    System.out.println("Describe your idea :");
                    propose.append(sc.nextLine() + "\n");
                    propose.append("#END\n");
                    return propose.toString();
                case "LISTIDEAS":
                    StringBuilder list = new StringBuilder();
                    list.append(decision + "\n");
                    list.append("#END\n");
                    return list.toString();
                case "#END":
                    break;
                default:
                    break;
            }
        } catch (InputMismatchException e) {
            return null;
        }

        return "#END\n";
    }

    public String listen(BufferedReader in){
        String msg = "";
        String line;
        try{
            while(!(line = in.readLine()).equals("#END")){
                msg +=line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static void main(String[] args) {
        new ClientTPNote(4444, "10.212.110.152").start();
    }


}
