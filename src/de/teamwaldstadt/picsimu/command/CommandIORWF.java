package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.GeneralRegister;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandIORWF extends CommandExecutor {
	
	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandIORWF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.isDestinationBitSet = Storage.extractBitsFromIntNumber(arguments, 0, 1, 8) == 1;
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 1, 8, 8);
	}

	@Override
	public void execute() throws Exception {
		GeneralRegister register = new GeneralRegister(this.fileRegister);
		int w = Main.STORAGE.getW();
		int f = Main.STORAGE.getRegister(register);
		int result = w | f;
		
		super.affectStatus(Command.IORWF, (result == 0x00 ? 0xFF : 0x00)); // z-flag is inverted for this command
		
		if (this.isDestinationBitSet) {
			Main.STORAGE.setRegister(register, result);
		} else {
			Main.STORAGE.setW(result);
		}
		
		super.incrementPC();
	}
	
	public boolean isDestinationBitSet() {
		return this.isDestinationBitSet;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
