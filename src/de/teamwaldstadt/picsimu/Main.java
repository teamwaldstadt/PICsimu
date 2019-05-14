package de.teamwaldstadt.picsimu;

import de.teamwaldstadt.picsimu.storage.Storage;

public class Main {
	
	public static final String PGM_NAME = "PICsimu";
	public static final String PGM_VERSION = "1.0.0";
	
	public static Storage STORAGE;
	public static CodeExecutor EXECUTOR;

	public static void main(String[] args) {
		System.out.println("Hello World, this is " + PGM_NAME + " " + PGM_VERSION);
		
		STORAGE = new Storage();
		EXECUTOR = new CodeExecutor();
	}

}
