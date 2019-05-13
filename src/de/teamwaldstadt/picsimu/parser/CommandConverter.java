package de.teamwaldstadt.picsimu.parser;

import de.teamwaldstadt.picsimu.command.Command;

public class CommandConverter {
	
	public static Command convert(int hexCode) {
		Command[] commandCodes = Command.values();

		//loop through all possible commands and check if one matches
		for (Command c : commandCodes) {
			
			int hex = hexCode & c.getMask();
			int com = c.getValue() & c.getMask();
			
			if (hex == com) {
				return c;
			}
		}
		return null;
	}
}
