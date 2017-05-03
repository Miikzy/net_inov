package server;

import protocole.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

	private int port;
	private ServerSocket server;
	private Socket client;

	public MyServer(int port) {

		try {
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.port = this.server.getLocalPort();

	}

	public void start() {

		try {

			this.client = this.server.accept();

			ObjectOutputStream writer = new ObjectOutputStream(this.client.getOutputStream());
			ObjectInputStream reader = new ObjectInputStream(this.client.getInputStream());

			Request received = this.read(reader);
			writer.writeObject(received);

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private Request read(ObjectInputStream reader) {

		Request received = null;

		try {

			Object object = reader.readObject();
			received = (Request) object;


		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
		}



		return received;

	}


}
