package edu.hendrix.csci250proj2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javafx.scene.image.Image;

public class DrawA {
		
		private String path;
		
		
		public static String readFile() throws IOException {
			Scanner in = new Scanner(new File("DrawAWhat.txt"));
			File document = new File("DrawAWhat.txt");
			FileReader read = new FileReader(document);
			String s = in.nextLine();
			
			read.close();
			return s;
			
		}
	
}
