package edu.hendrix.csci250proj2.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import edu.hendrix.csci250proj2.network.color;

import java.io.DataInputStream;
import java.io.DataOutputStream;


public class socketHelper {
	public Socket sock;
	private DataOutputStream dOut; 
	private DataInputStream dIn;
	private socketState state;
	private String username;

	public socketHelper(String hostName, int portNumber) throws UnknownHostException, IOException{
		    this.sock = new Socket(hostName, portNumber);
		    this.dOut = new DataOutputStream(sock.getOutputStream());
		    this.dIn = new DataInputStream(sock.getInputStream());
		    this.state = socketState.USERNAME;
	}
	
	public socketHelper(int portNumber) throws IOException{
		 ServerSocket serverSocket = new ServerSocket(portNumber);
		 this.sock = serverSocket.accept();
		 this.state = socketState.USERNAME;
		 if (this.sock != null && !this.sock.isClosed()) {
             /*
              * Get input and output streams
              */
			 this.dOut = new DataOutputStream(sock.getOutputStream());
			 this.dIn = new DataInputStream(sock.getInputStream());
         }
		 //serverSocket.close();
		 
	}
	
	public synchronized void writeString(String name) throws IOException{
		this.dOut.writeUTF(name);
		this.dOut.flush(); // Send off the data
	}
	
	public synchronized void writeColor(color sendCol) throws IOException{
		this.dOut.writeDouble(sendCol.getR());
		this.dOut.writeDouble(sendCol.getG());
		this.dOut.writeDouble(sendCol.getB());
		this.dOut.writeDouble(sendCol.getSX());
		this.dOut.writeDouble(sendCol.getSY());
		this.dOut.writeDouble(sendCol.getFX());
		this.dOut.writeDouble(sendCol.getFY());
		this.dOut.flush(); // Send off the data
	}
	
	public synchronized color readColor() throws IOException{
		double r = this.dIn.readDouble();
		double g = this.dIn.readDouble();
		double b = this.dIn.readDouble();
		double x = this.dIn.readDouble();
		double y = this.dIn.readDouble();
		double fx = this.dIn.readDouble();
		double fy = this.dIn.readDouble();
		return new color(r,g,b,x,y,fx,fy);
	}
	
	public synchronized void writeInt(int score) throws IOException{
		this.dOut.writeInt(score);
		this.dOut.flush();
	}
	
	public synchronized int readInt() throws IOException{
		int score = this.dIn.readInt();
		return score;
	}
	
	public synchronized String readNextString() throws IOException{
		return dIn.readUTF();
	}
	
	public synchronized socketState getState(){
		return state;
	}
	
	public synchronized void setState(socketState st){
		this.state = st;
	}
	
	public synchronized String getUsername(){
		return this.username;
		
	}
	
	public synchronized void setUsername(String user){
		this.username = user;
		
	}
	
}


