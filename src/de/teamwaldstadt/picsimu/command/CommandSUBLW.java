package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandSUBLW extends CommandExecutor {
	
	public CommandSUBLW(int arguments) throws Exception {
		super.setArguments(arguments);
	}
	
	@Override
	public void execute() throws Exception {
		int w = ((~Main.STORAGE.getW()) + 1) & 0xFF; // 2er-Komplement und maskieren
		int result = super.getArguments() + w;
		
		super.affectStatus(Command.SUBLW, result);
		super.affectStatusDC(Command.SUBLW, super.getArguments(), w);
		
		result &= 0xFF; // result maskieren
		
		Main.STORAGE.setW(result);
		super.incrementPC();
	}

}
