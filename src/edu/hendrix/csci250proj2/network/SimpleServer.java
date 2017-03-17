package edu.hendrix.csci250proj2.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class SimpleServer {
	private boolean quit = false;
	
	private ServerSocket accepter;
	private Consumer<Socket> handler;

	public SimpleServer(int port, Consumer<Socket> handler) throws IOException {
		accepter = new ServerSocket(port);
		this.handler = handler;
		System.out.println("Server: IP address: " + accepter.getInetAddress() + " (" + port + ")");
	}
	
	public void quit() {
		quit = true;
	}

	public void listen() throws IOException {
		while (!quit) {
			Socket s = accepter.accept();
			System.out.println("Server: Connection accepted from " + s.getInetAddress());
			new Thread(() -> handler.accept(s)).start();
		}
	}
	
	public static void sendMessageTo(Socket s, String msg) throws IOException {
        PrintWriter writer = new PrintWriter(s.getOutputStream());
        writer.print(msg);
        writer.flush();
	}
	
	public static String awaitMessageFrom(Socket s) throws IOException {
        BufferedReader responses = 
        		new BufferedReader(new InputStreamReader(s.getInputStream()));
        StringBuilder sb = new StringBuilder();
        while (!responses.ready()){}
        while (responses.ready()) {
            sb.append(responses.readLine() + '\n');
        }
        return sb.toString();
	}
}
