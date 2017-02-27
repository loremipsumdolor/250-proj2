package edu.hendrix.csci250proj2;

import javafx.scene.paint.Color;

public class User {
	
	//Created to encapsulate user data
	//Like their name, color, if they are done painting, ink, etc.
	//I needed to create this because when all users are done painting the GUI needs to switch
	
	//Feel free to edit this class as needed
	//I only have used the done variable
	
	private int ink;
	private Color color;
	private String name;
	private boolean done;
	
	public User() {
		this.name = "";
		this.color = Color.BLUE;
		this.done = false;
		this.ink = 100;
		
	}
	
	public void setName(String word) {
		name = word;
	}
	
	public void setColor(Color paint) {
		color = paint;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setDone() {
		done = true;
	}
	
	public void decreaseInk() {
		ink += -5;
	}
	
	public void increaseInk() {
		ink += 5;
	}
	
	public String getName() {
		return name;
	}
	

}
