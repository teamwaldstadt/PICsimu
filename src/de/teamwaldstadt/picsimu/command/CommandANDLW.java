package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandANDLW extends CommandExecutor {
	
	private int literal;
	
	public CommandANDLW(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.literal = Storage.extractBitsFromIntNumber(arguments, 8, 0);
	}

	@Override
	public void execute() throws Exception {
		int w = Main.STORAGE.getW();
		int result = w & this.literal;
		
		super.affectStatus(Command.ANDLW, result);
		
		Main.STORAGE.setW(result);
	}
	
	public int getLiteral() {
		return this.literal;
	}

}
