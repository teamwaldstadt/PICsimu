package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandSWAPF extends CommandExecutor {
	
	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandSWAPF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.isDestinationBitSet = Storage.extractBitsFromIntNumber(arguments, 0, 1, 8) == 1;
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 1, 8, 8);
	}

	@Override
	public void execute() throws Exception {
		int f = Main.STORAGE.getRegister(this.fileRegister, false);
		int newLowerNibble = (f & 0xF0) >> 4;
		int newUpperNibble = (f & 0x0F) << 4;
		int result = newUpperNibble + newLowerNibble;
		
		super.affectStatus(Command.SWAPF, result);
		
		if (this.isDestinationBitSet) {
			Main.STORAGE.setRegister(this.fileRegister, result, false);
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
