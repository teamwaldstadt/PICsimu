package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.GeneralRegister;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandBSF extends CommandExecutor {
	
	private int bitIndex;
	private int fileRegister;

	public CommandBSF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.bitIndex = Storage.extractBitsFromIntNumber(arguments, 2, 5, 12);
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 5, 12, 12);
	}

	@Override
	public void execute() throws Exception {
		try {
			GeneralRegister register = new GeneralRegister(this.fileRegister);
			
			Main.STORAGE.setBitOfRegister(register, bitIndex, true);
		} catch (Exception e) {
			SpecialRegister register = SpecialRegister.atAddress(fileRegister);
			
			Main.STORAGE.setBitOfRegister(register, bitIndex, true);
		}
		
		super.affectStatus(Command.BSF, 0);
		super.incrementPC();
	}
	
	public int getBitIndex() {
		return this.bitIndex;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
