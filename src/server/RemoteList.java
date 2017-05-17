package server;

import protocole.Answer;
import protocole.Idea;
import protocole.Participation;
import protocole.RemoteListInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RemoteList extends UnicastRemoteObject implements RemoteListInterface {

	private static int currentId = 0;
	private final List<Idea> ideas;

	private RemoteList(int port) throws RemoteException {
		super(port);
		this.ideas = new ArrayList<>();
	}

	@Override
	public Answer list() {
		List<Object> objects = new ArrayList<>(this.ideas);
		return new Answer(Answer.NORMAL_STATUS, objects);
	}

	@Override
	public Answer add(Idea idea) {
		Idea i = new Idea(idea);
		i.setId(currentId++);
		ideas.add(i);
		return new Answer(Answer.NORMAL_STATUS, null);
	}

	@Override
	public Answer participate(Participation p) {
		ideas.stream().filter(idea -> (idea.getId() == p.getId())).findFirst().orElseThrow(RuntimeException::new).addInterested(p.getEmail());
		return new Answer(Answer.NORMAL_STATUS, null);
	}

	@Override
	public Answer seeInterested(Integer id) {
		Idea i = ideas.stream().filter(idea -> (idea.getId() == id)).findFirst().orElse(null);
		if (i == null) {
			return new Answer(Answer.BAD_STATUS, null);
		}
		List<Object> objects = new ArrayList<>();
		objects.addAll(i.getInterested());
		return new Answer(Answer.NORMAL_STATUS, objects);
	}

	public static void main(String[] args) {
		try {

			Registry r = LocateRegistry.createRegistry(8080);
			System.out.println("Création de l'objet serveur...");
			RemoteList server = new RemoteList(8080);
			System.out.println("Référencement dans le RMIRegistry...");
			r.rebind("Server", server);
			System.out.println("Attente d'invocations - CTRL-C pour stopper");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}