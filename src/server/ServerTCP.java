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

		ServerSocket sSocket;
		Socket cSocket;
		while (true) {
			try {

				sSocket = new ServerSocket(this.port);
				System.out.println("attente de connection");
				cSocket = sSocket.accept();
				System.out.println("connecté");
				ObjectOutputStream output = new ObjectOutputStream(cSocket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(cSocket.getInputStream());

				while (true) {
					try {
						Request request = this.read(input);
						output.writeObject(this.send(request));
						System.out.println(this.remoteList.getIdeas().size());
					} catch (Exception e) {
						System.out.println("Mauvaise requete");
						break;
					}

				}

				cSocket.close();
				sSocket.close();

			} catch (IOException e) {
				System.out.println("fin/perte de connection");
				;
			}
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

	private Request read(ObjectInputStream reader) throws IOException, ClassNotFoundException {

		Object object = reader.readObject();
		Request received = (Request) object;

		return received;

	}

	public static void main(String... args) {
		new ServerTCP(8080).start();
	}

}
