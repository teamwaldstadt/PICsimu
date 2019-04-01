package de.teamwaldstadt.picsimu.command;

public class CommandConverter {
	
	public static AllCommands convert(int hexCode) {
		AllCommands[] commandCodes = AllCommands.values();
		//System.out.println(String.format("0x%08X", hexCode));
		for (AllCommands c : commandCodes) {
			
			//System.out.println(String.format("0x%08X", c.getValue()));
			String hex = String.format("0x%08X", hexCode >> c.getLength());
			String com = String.format("0x%08X", c.getValue() >> c.getLength());
			
			if (hex.equals(com)) {
				return c;
			}
		}
		return null;
	}
}
