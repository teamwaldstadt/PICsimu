package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.utils.Utils;

public class CommandBCF extends CommandExecutor {
	
	private int bitIndex;
	private int fileRegister;

	public CommandBCF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.bitIndex = Utils.extractBitsFromIntNumber(arguments, 7, 3);
		this.fileRegister = Utils.extractBitsFromIntNumber(arguments, 0, 7);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.setBitOfRegister(this.fileRegister, bitIndex, false, false);
		
		super.affectStatus(Command.BCF, 0);
		super.incrementPC();
	}
	
	public int getBitIndex() {
		return this.bitIndex;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
