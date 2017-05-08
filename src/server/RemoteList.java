package server;

import protocole.Answer;
import protocole.Idea;
import protocole.Participation;
import protocole.RemoteListInterface;

import java.util.ArrayList;
import java.util.List;

public class RemoteList implements RemoteListInterface {

	private static int currentId = 0;
	private final List<Idea> ideas;

	public RemoteList() {
		this.ideas = new ArrayList<>();
	}

	public Answer list() {
		List<Object> objects = new ArrayList<>(this.ideas);
		return new Answer(Answer.NORMAL_STATUS, objects);
	}

	public Answer add(Idea idea) {
		Idea i = new Idea(idea);
		i.setId(currentId++);
		ideas.add(i);
		return new Answer(Answer.NORMAL_STATUS, null);
	}

	public Answer participate(Participation p) {
		ideas.stream().filter(idea -> (idea.getId() == p.getId())).findFirst().orElseThrow(RuntimeException::new).addInterested(p.getEmail());
		return new Answer(Answer.NORMAL_STATUS, null);
	}

	public Answer seeInterested(Integer id) {
		Idea i = ideas.stream().filter(idea -> (idea.getId() == id)).findFirst().orElse(null);
		if (i == null) {
			return new Answer(Answer.BAD_STATUS, null);
		}
		List<Object> objects = new ArrayList<>();
		objects.addAll(i.getInterested());
		return new Answer(Answer.NORMAL_STATUS, objects);
	}

	public List<Idea> getIdeas() {
		return ideas;
	}
}