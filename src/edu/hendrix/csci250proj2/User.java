package edu.hendrix.csci250proj2;

import javafx.scene.paint.Color;

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
	private Color color;
	private int ink;
	private boolean done;
	
	public User(String name) {
		this.name = name;
		this.color = Color.TRANSPARENT;
		this.done = false;
		this.ink = 100;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color paint) {
		color = paint;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public void setDone() {
		done = true;
	}
	
	public int getInk() {
		return ink;
	}

	private void setInk(int ink) {
		this.ink = ink;
	}
	
	public void decreaseInk() {
		setInk(getInk() - 5);
	}
	
	public void increaseInk() {
		setInk(getInk() + 5);
	}
}
