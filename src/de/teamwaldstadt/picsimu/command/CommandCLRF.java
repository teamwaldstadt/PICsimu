package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.GeneralRegister;

public class CommandCLRF extends CommandExecutor {
	
	public CommandCLRF(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		GeneralRegister register = new GeneralRegister(super.getArguments());
		
		Main.STORAGE.setRegister(register, 0x00);
		super.affectStatus(Command.CLRF, 0x00);
		super.incrementPC();
	}

}
