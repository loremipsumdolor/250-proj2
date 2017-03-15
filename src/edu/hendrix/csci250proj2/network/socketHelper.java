package edu.hendrix.csci250proj2.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import edu.hendrix.csci250proj2.gui.PaintingGameController;

import java.io.DataInputStream;
import java.io.DataOutputStream;


public class socketHelper {
	public Socket sock;
	private DataOutputStream dOut; 
	private DataInputStream dIn;
	private socketState state;

	public socketHelper(String hostName, int portNumber) throws UnknownHostException, IOException{
		    this.sock = new Socket(hostName, portNumber);
		    this.dOut = new DataOutputStream(sock.getOutputStream());
		    this.dIn = new DataInputStream(sock.getInputStream());
	}
	
	public socketHelper(int portNumber) throws IOException{
		 ServerSocket serverSocket = new ServerSocket(portNumber);
		 this.sock = serverSocket.accept();
		 if (this.sock != null && !this.sock.isClosed()) {
             /*
              * Get input and output streams
              */
			 this.dOut = new DataOutputStream(sock.getOutputStream());
			 this.dIn = new DataInputStream(sock.getInputStream());
         }
		 
		 
	}
	
	public void writeString(String name) throws IOException{
		this.dOut.writeUTF(name);
		this.dOut.flush(); // Send off the data
	}
	
	public synchronized String readNextString() throws IOException{
		System.out.println("readNextResult" + dIn.readUTF() );
		/*
		String Name = null;
		while(dIn.readUTF() != null){
			
			Name += dIn.readUTF();
		}
		
		return Name;
		*/return "blah";
	}
	
	public socketState getState(){
		return state;
	}
	
	public void setState(socketState st){
		this.state = st;
	}
}


