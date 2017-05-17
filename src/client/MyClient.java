package client;

import protocole.Answer;
import protocole.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 4 stages of a client's life :
 * 1. Socket's instantiation;
 * 2. Connect the socket with the service server's side targeted;
 * 3. Exchange of data with server's sockets;
 * 4. Close the socket when the communications are done.
 */
public class MyClient {

	private int port;
	private String host;

	public MyClient(int port, String host) {
		this.port = port;
		this.host = host;
	}

	public void start() {
		try {
			//Step 1;
			Socket socket = new Socket(host, port);

			//Step 2;
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			//Step 3;
			while (true) {
				Request request = new RequestCreator().create();
				if (request == null) {
					System.out.println("There is no request.");
					break;
				}
				out.writeObject(request);
				Answer answer = (Answer) in.readObject();
				if (!(answer.getStatus().equals(Answer.NORMAL_STATUS))) {
					System.out.println("An error occured in the answer.");
					return;
				}
				if (answer.getAnswer() == null) {
					System.out.println("Request successful");
				} else {
					System.out.println("Answer: \n" + answer.getAnswer());
				}
			}

			socket.close();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new MyClient(8080, "10.212.97.234").start();
	}
}
