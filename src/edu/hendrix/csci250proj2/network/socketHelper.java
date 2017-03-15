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
	
	//For testing purposes'
	public Socket getSock() {
		return this.sock;
	}
	
	public synchronized void writeString(String name) throws IOException{
		this.dOut.writeUTF(name);
		this.dOut.flush(); // Send off the data
	}
	
	public synchronized void writeColor(int col, int x, int y) throws IOException{
		this.dOut.writeInt(col);
		this.dOut.writeInt(x);
		this.dOut.writeInt(y);
		this.dOut.flush(); // Send off the data
	}
	
	public synchronized colorStruct readColor() throws IOException{
		int col = this.dIn.readInt();
		int x = this.dIn.readInt();
		int y = this.dIn.readInt();
		return new colorStruct(col,x,y);
	}
	
	public synchronized String readNextString() throws IOException{
		//System.out.println("readNextResult" + dIn.readUTF() );
		//System.out.flush();
		/*
		String Name = null;
		while(dIn.readUTF() != null){
			
			Name += dIn.readUTF();
		}
		
		return Name;
		*/return dIn.readUTF();
	}
	
	public synchronized socketState getState(){
		return state;
	}
	
	public synchronized void setState(socketState st){
		this.state = st;
	}
	
	public class colorStruct{
		int color;
		int x;
		int y;
		
		colorStruct(int c, int x, int y){
			this.color = c;
			this.x = x;
			this.y = y;
		}
		
		int getCol(){
			return this.color;
		}
		
		int getX(){
			return this.x;
		}
		
		int getY(){
			return this.y;
		}
	}
	
	
}


