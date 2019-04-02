package de.teamwaldstadt.picsimu.command;

public class CommandConverter {
	
	public static Command convert(int hexCode) {
		Command[] commandCodes = Command.values();

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
