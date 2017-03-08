package edu.hendrix.csci250proj2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DrawA {
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
