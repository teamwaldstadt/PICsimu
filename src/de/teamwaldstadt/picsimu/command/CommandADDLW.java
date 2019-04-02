package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandADDLW extends CommandExecutor {
	
	private int literal;
	
	public CommandADDLW(int literal) throws Exception {
		Storage.checkNotAByte(literal);
		
		this.literal = literal;
	}

	@Override
	public void execute() throws Exception {
		int w = Main.STORAGE.getW();
		int result = w + this.literal;
		
		super.affectStatus(Command.ADDLW, result);
		
		Main.STORAGE.setW(result);
	}
	
	public int getLiteral() {
		return this.literal;
	}

}
