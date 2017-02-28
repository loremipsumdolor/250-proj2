package edu.hendrix.csci250proj2;

public class User {
	
	/*
	 * Created to encapsulate user data
	 * Like their name, color, if they are done painting, ink, etc.
	 * I needed to create this because when all users are done painting the GUI needs to switch
	 * 
	 * Feel free to edit this class as needed
	 * I only have used the done variable
	 */

	private String name;
	private boolean done;
	
	public User(String name) {
		this.name = name;
		this.done = false;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public void setDone() {
		done = true;
	}
}
