package edu.hendrix.csci250proj2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import edu.hendrix.csci250proj2.network.socketHelper;
import javafx.scene.control.Alert.AlertType;

public class Testing {

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
	public void testsocketHelper() {
		//Checks creation
		try {
			socketHelper socket = new socketHelper("10.253.200.180", 3002);
			//Test getState()
			
			//Test colorConstruct
		
		
		} catch (IOException e) {
			fail("Invalid IP");
		}
		
	}

}
