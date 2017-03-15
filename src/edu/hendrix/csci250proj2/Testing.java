package edu.hendrix.csci250proj2;
/*
import static org.junit.Assert.*;

<<<<<<< HEAD
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
=======
>>>>>>> parent of 4421185... Test Cases Started

import org.junit.Test;

import javafx.scene.control.Alert.AlertType;

public class Testing {
	
	public final static int PORT = 8888;
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
<<<<<<< HEAD
	
	@Test
	public void testsocketHelper() throws IOException{
		
		try {
		socketHelper socketClient = new socketHelper(PORT);			
		Socket temp = socketClient.getSock();
		String s = temp.getInetAddress() + "";
		socketHelper socketHost = new socketHelper(s, PORT);
			
		} catch (IOException e) {
			fail("Invalid IP or PORT");
		}
		
	}
}
	
	
	//socketHelper socketClient = new socketHelper(3002);
	//socketClient.writeString("Testing 123");
	//assertEquals(socketHost.readNextString(), "Testing 123");
	//Start up your server that is listening on the port
	//Test your client code who will contact local host with whatever your port number is
	//Then just do your test, test your things sent equals the thing you received

=======

}
*/
>>>>>>> parent of 4421185... Test Cases Started
