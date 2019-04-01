package de.teamwaldstadt.picsimu;

import de.teamwaldstadt.picsimu.parser.Parser;

public class Main {

	/*
	 * Main class
	 */
	
	public static final String PGM_NAME = "PICsimu";
	public static final String PGM_VERSION = "0.0.1";

	public static void main(String[] args) {
		System.out.println("Hello World, this is " + PGM_NAME + " " + PGM_VERSION);
		Parser p = new Parser();

		try {
			String[] lines = p.loadFile("C:\\Users\\Service\\Documents\\RECHNERTECHNIK\\TPicSim\\TPicSim1.LST");
			for (String line : lines) {
				line = line.replaceAll(";.*", "");
				System.out.println(line);
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

}
