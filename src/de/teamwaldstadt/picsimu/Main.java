package de.teamwaldstadt.picsimu;

import de.teamwaldstadt.picsimu.storage.Storage;

public class Main {
	
	public static final String PGM_NAME = "PICsimu";
	public static final String PGM_VERSION = "0.0.1";
	
	public static Storage STORAGE;

	public static void main(String[] args) {
		System.out.println("Hello World, this is " + PGM_NAME + " " + PGM_VERSION);
		
		STORAGE = new Storage();
		
		new CodeExecutor();
	}

}
