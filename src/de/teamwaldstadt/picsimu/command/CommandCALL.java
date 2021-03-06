package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;

public class CommandCALL extends CommandExecutor {
	
	public CommandCALL(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		// lade (PC + 1) auf den stack
		Main.STORAGE.getStack().push(Main.STORAGE.getPC() + 1);
		
		// setze PC auf die Adresse der Routine
		Main.STORAGE.jumpPC(super.getArguments());
		
		super.affectStatus(Command.CALL, 0);
	}

}
