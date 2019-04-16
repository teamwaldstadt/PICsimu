package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class CommandRETFIE extends CommandExecutor {
	
	public CommandRETFIE(int arguments) throws Exception {
		super.setArguments(arguments);
	}
	
	@Override
	public void execute() throws Exception {
		int tos = Main.STORAGE.getStack().pop();
		
		// lade tos in PC
		Main.STORAGE.jumpPC(tos);
		
		// setze GIE bit im INTCON register
		Main.STORAGE.setBitOfRegister(SpecialRegister.INTCON.getAddress(), 7, true, false);
		
		super.affectStatus(Command.RETFIE, 0);
	}

}
