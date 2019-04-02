package de.teamwaldstadt.picsimu;

import de.teamwaldstadt.picsimu.command.CommandSet;
import de.teamwaldstadt.picsimu.gui.GUIWindow;
import de.teamwaldstadt.picsimu.parser.Parser;
import de.teamwaldstadt.picsimu.storage.Storage;

public class Main {
	
	public static final String PGM_NAME = "PICsimu";
	public static final String PGM_VERSION = "0.0.1";
	
	public static Storage STORAGE;

	public static void main(String[] args) {
		System.out.println("Hello World, this is " + PGM_NAME + " " + PGM_VERSION);
		
		STORAGE = new Storage();
		Parser p = new Parser();
		CommandSet[] commands = null;
		
		try {
			commands = p.getCommandList("C:\\Users\\Service\\Documents\\RECHNERTECHNIK\\TPicSim\\TPicSim1.LST");
		} catch (Exception e) {
			System.exit(0);
		}
		
		for (CommandSet c : commands) {
			System.out.println("Run command: " + c.getCommand());
			
			try {
				if (c.getCommand().getExecutor().getConstructors()[0].getParameterTypes().length == 0) {
					System.out.println("not yet defined");
				} else {
					c.getCommand().getExecutor().getConstructor(Integer.class).newInstance(c.getArgument());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		new GUIWindow();
	}

}
