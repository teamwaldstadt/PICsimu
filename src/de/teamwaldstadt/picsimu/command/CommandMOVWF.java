package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandMOVWF extends CommandExecutor {
	
	public CommandMOVWF(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		int w = Main.STORAGE.getW();
		
		Main.STORAGE.setRegister(super.getArguments(), w, false);
		
		super.affectStatus(Command.MOVWF, 0);
	}

}
