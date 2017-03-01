package edu.hendrix.csci250proj2.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class SocketHelper {
	public Socket sock;
	public DataOutputStream dOut; 
	public DataInputStream dIn; 

	public SocketHelper(String hostName, int portNumber) throws UnknownHostException, IOException{
		this.sock = new Socket(hostName, portNumber);
		this.dOut = new DataOutputStream(sock.getOutputStream());
		this.dIn = new DataInputStream(sock.getInputStream());
	}
	
	public SocketHelper(int portNumber) throws IOException{
		ServerSocket serverSocket = new ServerSocket(portNumber);
		while(true){
			this.sock = serverSocket.accept();
		 if (this.sock != null ) break;
		 }
		 this.dOut = new DataOutputStream(sock.getOutputStream());
		 this.dIn = new DataInputStream(sock.getInputStream());
	}
	
	public void writeUserName(String name) throws IOException{
		this.dOut.writeUTF(name);
		this.dOut.flush(); // Send off the data
	}
	
	public String readUserName() throws IOException{
		String Name = dIn.readUTF();
		return Name;
	}
	
}