package edu.hendrix.csci250proj2.network;

public class MessageHolder {
	private String msg;
	
	public MessageHolder() {
		msg = null;
	}
	
	public void hold(String s) {
		msg = s;
	}
	
	public boolean has() {return msg != null;}
	
	public String get() {return msg;}
}
