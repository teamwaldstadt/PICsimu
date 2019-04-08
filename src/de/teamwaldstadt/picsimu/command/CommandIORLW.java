package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandIORLW extends CommandExecutor {
	
	public CommandIORLW(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		int w = Main.STORAGE.getW();
		int result = w | super.getArguments();

		super.affectStatus(Command.IORLW, result);
		
		Main.STORAGE.setW(result);
	}

}
