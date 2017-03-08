package edu.hendrix.csci250proj2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class DrawSelect {
	public static String initialize() throws FileNotFoundException {
		String chosenPrompt = "";
		ArrayList<String> potentialDrawings = readFile();
		chosenPrompt = potentialDrawings.get(ThreadLocalRandom.current().nextInt(potentialDrawings.size()));
		return chosenPrompt;
	}
	
	public static ArrayList<String> readFile() throws FileNotFoundException {
		ArrayList<String> potentialDrawings = new ArrayList<String>();
		Scanner fileScanner = new Scanner(new File("./resources/DrawAWhat.txt"));
		while (fileScanner.hasNextLine()) {
			potentialDrawings.add(fileScanner.nextLine());
		}
		fileScanner.close();
		return potentialDrawings;
	}
}
