package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.GeneralRegister;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandADDWF extends CommandExecutor {
	
	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandADDWF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		boolean isDestinationBitSet = Storage.extractBitsFromIntNumber(arguments, 8, 7) == 1;
		int fileRegister = Storage.extractBitsFromIntNumber(arguments, 7, 0);;
		
		Storage.check7Bits(fileRegister);
		
		this.isDestinationBitSet = isDestinationBitSet;
		this.fileRegister = fileRegister;
	}

	@Override
	public void execute() throws Exception {
		GeneralRegister register = new GeneralRegister(this.fileRegister);
		int w = Main.STORAGE.getW();
		int f = Main.STORAGE.getRegister(register);
		int result = w + f;
		
		super.affectStatus(Command.ADDWF, result);
		
		if (this.isDestinationBitSet) {
			Main.STORAGE.setRegister(register, result);
		} else {
			Main.STORAGE.setW(result);
		}
	}
	
	public boolean isDestinationBitSet() {
		return this.isDestinationBitSet;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
