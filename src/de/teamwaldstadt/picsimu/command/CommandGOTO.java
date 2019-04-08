package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandGOTO extends CommandExecutor {
	
	public CommandGOTO(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		super.affectStatus(Command.GOTO, 0);
		Main.STORAGE.setPC(super.getArguments());
	}

}
