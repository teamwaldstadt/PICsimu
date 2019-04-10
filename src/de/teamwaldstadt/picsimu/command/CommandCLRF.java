package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandCLRF extends CommandExecutor {
	
	public CommandCLRF(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.setRegister(super.getArguments(), 0x00, false);
		
		super.affectStatus(Command.CLRF, 0x00);
		super.incrementPC();
	}

}
