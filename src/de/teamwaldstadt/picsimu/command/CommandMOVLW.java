package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandMOVLW extends CommandExecutor {
	
	public CommandMOVLW(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		int result = super.getArguments();
		
		super.affectStatus(Command.MOVLW, result);
		Main.STORAGE.setW(result);
	}

}
