package edu.hendrix.csci250proj2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DrawSelect extends DrawA{
	public static String initialize() throws FileNotFoundException {
		String chosenPrompt = "";
		ArrayList<String> potentialDrawings = DrawA.readFile();
		chosenPrompt = potentialDrawings.get(ThreadLocalRandom.current().nextInt(potentialDrawings.size()));
		return chosenPrompt;
	}

}
