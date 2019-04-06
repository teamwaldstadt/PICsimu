package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandMOVLW extends CommandExecutor {
	private int literal;
	
	public CommandMOVLW(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.literal = arguments;
		//this.literal = Storage.extractBitsFromIntNumber(arguments, 8, 0);
	}

	@Override
	public void execute() throws Exception {
		super.affectStatus(Command.MOVLW, this.literal);
		Main.STORAGE.setW(this.literal);
	}
	
	public int getLiteral() {
		return this.literal;
	}

}
