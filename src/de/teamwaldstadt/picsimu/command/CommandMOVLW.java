package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandMOVLW extends CommandExecutor {
	int literal;
	public CommandMOVLW(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.literal = arguments;
		//this.literal = Storage.extractBitsFromIntNumber(arguments, 8, 0);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.setW(literal);
	}

}
