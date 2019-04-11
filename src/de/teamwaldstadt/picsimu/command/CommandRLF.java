package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;
import de.teamwaldstadt.picsimu.storage.Status;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandRLF extends CommandExecutor {

	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandRLF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.isDestinationBitSet = Storage.extractBitsFromIntNumber(arguments, 0, 1, 8) == 1;
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 1, 8, 8);
	}

	@Override
	public void execute() throws Exception {
		int f = Main.STORAGE.getRegister(this.fileRegister, false);
		int result = f << 1;
		
		// Rotation durch Carry: wenn Carry gesetzt war, wird das LSB von result gesetzt
		if (Main.STORAGE.isBitOfRegisterSet(SpecialRegister.STATUS.getAddress(), Status.C.getBitIndex(), false)) {
			result += 1;
		}
		
		// Rotation durch Carry: wenn linksshift > 0xFF ergibt, wird das Carry gesetzt
		super.affectStatus(Command.RLF, result);
		
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
