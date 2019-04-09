package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.GeneralRegister;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class CommandMOVWF extends CommandExecutor {
	
	public CommandMOVWF(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		int w = Main.STORAGE.getW();
		
		try {
			GeneralRegister register = new GeneralRegister(super.getArguments());
			
			Main.STORAGE.setRegister(register, w);
			super.affectStatus(Command.MOVWF, 0);
		} catch (Exception e) {
			SpecialRegister register = SpecialRegister.atAddress(super.getArguments());
			
			Main.STORAGE.setRegister(register, w);
			super.affectStatus(Command.MOVWF, 0);
		}

		super.affectStatus(Command.MOVWF, 0);
		super.incrementPC();
	}

}
