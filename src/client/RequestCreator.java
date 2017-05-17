package client;

import protocole.Idea;
import protocole.Participation;
import protocole.Request;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class RequestCreator {

	String msg = "Write :\n"
			+ "\t\"add\", if you want to create an idea;\n"
			+ "\t\"list\", if you want to display all the ideas;\n"
			+ "\t\"participate\", if you want to participate to an idea;\n"
			+ "\t\"team\", if you want to see the volunteers about an idea.\n"
			+ "\t anything to quit.";

	public Request create() {
		Scanner sc = new Scanner(System.in);
		Request req = new Request();
		System.out.println(msg);

		try {
			String decision = sc.nextLine();
			switch (decision) {
				case "add":
					System.out.println("What's your name?");
					String creator = sc.nextLine();
					System.out.println("What's your email address?");
					String email = sc.nextLine();
					System.out.println("What's the name of your idea?");
					String name = sc.nextLine();
					System.out.println("Describe your idea :");
					String description = sc.nextLine();
					System.out.println("What technologies would be involved in the project?\n" +
							" Format : tech1, tech2, tech3 ...");
					List<String> technologies = Arrays.asList(sc.nextLine().split(","));
					req = new Request(new Idea(creator, email, name, description, technologies));
					break;
				case "list":
					req = new Request();
					break;
				case "participate":
					System.out.println("Which idea do you want to participate to?");
					int id = sc.nextInt();
					sc.nextLine();
					System.out.println("What's your email address?");
					req = new Request(new Participation(id, sc.nextLine()));
					break;
				case "team":
					System.out.println("Which idea do you want to see the participant from?");
					req = new Request(sc.nextInt());
					break;
				default:
					break;
			}
		} catch (InputMismatchException e) {
			return null;
		}

		return req;

	}
}
