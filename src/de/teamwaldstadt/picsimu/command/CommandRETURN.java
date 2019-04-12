package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandRETURN extends CommandExecutor {
	
	public CommandRETURN(int arguments) throws Exception {
		super.setArguments(arguments);
	}
	
	@Override
	public void execute() throws Exception {
		int tos = Main.STACK.pop();
		
		// lade tos in PC
		Main.STORAGE.setPC(tos);
		
		super.affectStatus(Command.RETURN, 0);
	}

}
