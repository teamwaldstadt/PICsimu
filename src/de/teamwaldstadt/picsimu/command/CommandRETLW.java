package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandRETLW extends CommandExecutor {
	
	public CommandRETLW(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		int tos = Main.STACK.pop();
		
		// lade literal in w
		Main.STORAGE.setW(super.getArguments());
		
		// lade tos in PC
		Main.STORAGE.jumpPC(tos);
		
		super.affectStatus(Command.RETLW, 0);
	}

}
