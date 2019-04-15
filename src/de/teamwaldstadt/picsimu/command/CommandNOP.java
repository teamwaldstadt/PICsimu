package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandNOP extends CommandExecutor {
	
	public CommandNOP(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		// No operation
		
		super.affectStatus(Command.NOP, 0);
	}

}
