package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandADDLW extends CommandExecutor {
	
	public CommandADDLW(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		int w = Main.STORAGE.getW();
		int result = super.getArguments() + w;
		
		super.affectStatus(Command.ADDLW, result);
		super.affectStatusDC(Command.ADDLW, super.getArguments(), w);
		
		result &= 0xFF; // result maskieren
		
		Main.STORAGE.setW(result);
		super.incrementPC();
	}

}
