package de.teamwaldstadt.picsimu;

import java.util.HashMap;

import de.teamwaldstadt.picsimu.gui.GUIWindow;
import de.teamwaldstadt.picsimu.command.Command;
import de.teamwaldstadt.picsimu.parser.Parser;
import de.teamwaldstadt.picsimu.storage.Storage;

public class Main {
	
	public static final String PGM_NAME = "PICsimu";
	public static final String PGM_VERSION = "0.0.1";
	
	public static Storage STORAGE;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println("Hello World, this is " + PGM_NAME + " " + PGM_VERSION);
		
		STORAGE = new Storage();
		Parser p = new Parser();

		try {
			HashMap<Integer, Command>[] lines = p.getCommandList("C:\\Users\\Service\\Documents\\RECHNERTECHNIK\\TPicSim\\TPicSim10.LST");
			
		} catch (Exception e) {
			System.exit(0);
		}
		
		GUIWindow gui = new GUIWindow();
	}

}
