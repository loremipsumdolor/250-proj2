package edu.hendrix.csci250proj2.network;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;

import org.junit.Test;

public class SimpleServerTest {

	public final static int PORT = 8888;
	public final static String TEST_MSG = "Hello there!";
	
	@Test
	public void test() throws IOException {
		MessageHolder holder = new MessageHolder();
		SimpleServer server = new SimpleServer(PORT, s -> {
			try {
				System.out.println("Received contact");
				String msg = SimpleServer.awaitMessageFrom(s);
				System.out.println("msg: " + msg);
				holder.hold(msg.trim());
				System.out.println("Finished successfully.");
			} catch (Exception e) {
				System.out.println("Here!");
				e.printStackTrace();
			}
		});
		
		new Thread(() -> {
			try {
				server.listen();
			} catch (Exception e) {
				System.out.println("There!");
				e.printStackTrace();
			}}).start();
		
		Socket sender = new Socket("localhost", PORT);
		SimpleServer.sendMessageTo(sender, TEST_MSG);
		System.out.println("Message sent");
		sender.close();
		
		while (!holder.has()) {}
		assertEquals(TEST_MSG, holder.get());
	}

}
