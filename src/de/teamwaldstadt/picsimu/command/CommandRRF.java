package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;
import de.teamwaldstadt.picsimu.storage.Status;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandRRF extends CommandExecutor {

	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandRRF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.isDestinationBitSet = Storage.extractBitsFromIntNumber(arguments, 0, 1, 8) == 1;
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 1, 8, 8);
	}

	@Override
	public void execute() throws Exception {
		int f = Main.STORAGE.getRegister(this.fileRegister, false);
		boolean overflow = false;
		
		// Rotation durch Carry: overflow wenn LSB von f gesetzt war
		if (Main.STORAGE.isBitOfRegisterSet(this.fileRegister, 0, false)) {
			overflow = true;
		}
		
		int result = f >> 1;
		
		// Rotation durch Carry: MSB von result setzen, wenn Carry gesetzt war
		if (Main.STORAGE.isBitOfRegisterSet(SpecialRegister.STATUS.getAddress(), Status.C.getBitIndex(), false)) {
			result |= (1 << 7);
		} else {
			result &= ~(1 << 7);
		}
		
		// Rotation durch Carry: wenn overflow, dann wird Carry gesetzt
		if (overflow) {
			result += 0x100;
		}
		
		super.affectStatus(Command.RRF, result);
		
		result &= 0xFF; // result maskieren
		
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
