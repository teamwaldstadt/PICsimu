package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandCLRW extends CommandExecutor {
	
	public CommandCLRW(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		Main.STORAGE.setW(0x00);
		
		super.affectStatus(Command.CLRW, 0x00);
	}

}
