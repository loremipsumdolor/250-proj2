package edu.hendrix.csci250proj2;

import static org.junit.Assert.*;


import org.junit.Test;

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

}
