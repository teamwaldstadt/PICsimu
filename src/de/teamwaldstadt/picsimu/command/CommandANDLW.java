package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandANDLW extends CommandExecutor {
	
	public CommandANDLW(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		int w = Main.STORAGE.getW();
		int result = w & super.getArguments();

		super.affectStatus(Command.ANDLW, result);
		
		Main.STORAGE.setW(result);
	}

}
