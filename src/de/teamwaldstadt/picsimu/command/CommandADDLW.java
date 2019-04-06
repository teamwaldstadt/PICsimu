package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandADDLW extends CommandExecutor {
	
	private int literal;
	
	public CommandADDLW(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.literal = arguments;
		// this.literal = Storage.extractBitsFromIntNumber(arguments, 8, 0);
	}

	@Override
	public void execute() throws Exception {
		int w = Main.STORAGE.getW();
		int result = w + this.literal;
		
		super.affectStatus(Command.ADDLW, result);
		
		Main.STORAGE.setW(result);
	}
	
	public int getLiteral() {
		return this.literal;
	}

}
