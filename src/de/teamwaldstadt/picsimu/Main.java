package de.teamwaldstadt.picsimu;

import java.util.HashMap;

import de.teamwaldstadt.picsimu.command.AllCommands;
import de.teamwaldstadt.picsimu.parser.Parser;

public class Main {
	
	public static final String PGM_NAME = "PICsimu";
	public static final String PGM_VERSION = "0.0.1";

	public static void main(String[] args) {
		System.out.println("Hello World, this is " + PGM_NAME + " " + PGM_VERSION);
		Parser p = new Parser();

		try {
			HashMap<Integer, AllCommands>[] lines = p.getCommandList("C:\\Users\\Service\\Documents\\RECHNERTECHNIK\\TPicSim\\TPicSim1.LST");
			System.out.println(lines[0].get(0));
		} catch (Exception e) {
			System.exit(0);
		}
	}

}
