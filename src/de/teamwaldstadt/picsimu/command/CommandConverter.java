package de.teamwaldstadt.picsimu.command;

public class CommandConverter {
	
	public static CommandNames convert(int hexCode) {
		CommandNames[] commandCodes = CommandNames.values();

		for (CommandNames c : commandCodes) {
			
			int hex = hexCode & c.getMask();
			int com = c.getValue() & c.getMask();
			
			if (hex == com) {
				return c;
			}
		}
		return null;
	}
}
