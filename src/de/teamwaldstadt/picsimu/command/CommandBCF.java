package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.GeneralRegister;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandBCF extends CommandExecutor {
	
	private int bitIndex;
	private int fileRegister;

	public CommandBCF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.bitIndex = Storage.extractBitsFromIntNumber(arguments, 10, 7);
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 7, 0);
	}

	@Override
	public void execute() throws Exception {
		try {
			GeneralRegister register = new GeneralRegister(this.fileRegister);
			
			Main.STORAGE.setBitOfRegister(register, bitIndex, false);
		} catch (Exception e) {
			SpecialRegister register = SpecialRegister.atAddress(fileRegister);
			
			Main.STORAGE.setBitOfRegister(register, bitIndex, false);
		}
		
		super.affectStatus(Command.BCF, 0);
	}
	
	public int getBitIndex() {
		return this.bitIndex;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
