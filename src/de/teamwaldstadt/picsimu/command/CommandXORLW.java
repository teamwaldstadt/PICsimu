package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandXORLW extends CommandExecutor {
	
	public CommandXORLW(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		int w = Main.STORAGE.getW();
		int result = w ^ super.getArguments();

		super.affectStatus(Command.XORLW, result);
		
		Main.STORAGE.setW(result);
	}

}
