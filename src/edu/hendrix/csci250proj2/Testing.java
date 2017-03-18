package edu.hendrix.csci250proj2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Ignore;
import org.junit.Test;

import edu.hendrix.csci250proj2.network.socketHelper;
import edu.hendrix.csci250proj2.network.socketState;
import javafx.scene.control.Alert.AlertType;

public class Testing {
	
	public final static int PORT = 8883;
	public final static String TEST_MSG = "Hello there!";

	@Test
	public void testPromptString() {
		try {
			String prompt = DrawSelect.initialize();
			if (prompt instanceof String) {
			  assertEquals("","");
			}else {
				fail("Not a string!");
			}
		} catch (Exception exc) {
			fail("Exception caught");
		}
		
	}
	
	@Test
	public void testUser() {
		User chantal = new User("Chantal");
		assertEquals(chantal.getName(), "Chantal");
		assertEquals(chantal.isDone(), false);
		chantal.setDone();
		assertEquals(chantal.isDone(), true);
	}
	
	@Test
	public void testsocketHelper() throws IOException{
/*		socketHelper socketClient = new socketHelper(PORT,  s -> {
			try {
				String msg = socketHelper.readNextString();
				System.out.println("recieved contact: " + msg);
				
			}catch (exception e) {
				System.out.println("Failed socketHelper connect");
			}
		});*/
		new Thread(() -> {
			try {
				socketHelper socketClient = new socketHelper(PORT);
			} catch (Exception e) {
			
		}}).start();
		new Thread(() -> {
			try {
				String s = socketClient.readNextString();
				System.out.println("recieved a string" + s);
			} catch (Exception e) {
				System.out.println("Fail socketClient connect");
			}}).start();
		
		socketHelper sender = new socketHelper("localhost", PORT);
		sender.writeString("This won't be recieved as a string");
		
	}

	@Ignore
	@Test
	public void testSocketState() throws IOException {
		socketHelper socket = new socketHelper(PORT);			
		socket.setState(socketState.DRAWING);
		assertEquals(socketState.DRAWING, socket.getState());
		
	}
	
}