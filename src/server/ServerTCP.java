package server;

import protocole.Answer;
import protocole.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {

	private int port;
	private RemoteList remoteList;

	public ServerTCP(int port) {
		this.port = port;
		this.remoteList = new RemoteList();
	}

	public void start() {

		try {

			ServerSocket sSocket = new ServerSocket(this.port);
			System.out.println("attente de connection");
			Socket cSocket = sSocket.accept();
			System.out.println("connecté");
			ObjectOutputStream output = new ObjectOutputStream(cSocket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(cSocket.getInputStream());

			while (true) {

				Request request = this.read(input);
				output.writeObject(this.send(request));
				System.out.println(this.remoteList.getIdeas().size());

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Answer send(Request request) {

		try {
			if (request.getParam() == null) {
				return (Answer) (Class.forName(remoteList.getClass().getName()).getMethod(request.getMethod())).invoke(remoteList);
			} else {
				return (Answer) (Class.forName(remoteList.getClass().getName()).getMethod(request.getMethod(), request.getParam().getClass()).invoke(remoteList, request.getParam()));
			}
		} catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
			e.printStackTrace();
			return new Answer(Answer.BAD_STATUS, null);
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
