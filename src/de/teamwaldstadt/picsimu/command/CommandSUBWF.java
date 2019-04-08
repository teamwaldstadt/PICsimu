package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.GeneralRegister;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandSUBWF extends CommandExecutor {
	
	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandSUBWF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.isDestinationBitSet = Storage.extractBitsFromIntNumber(arguments, 8, 7) == 1;
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 7, 0);
	}

	@Override
	public void execute() throws Exception {
		GeneralRegister register = new GeneralRegister(this.fileRegister);
		int w = ((Main.STORAGE.getW() ^ 0xFF) + 1) & 0xFF; // 2er-Komplement und maskieren
		int f = Main.STORAGE.getRegister(register);
		int result = f + w;
		
		super.affectStatus(Command.SUBWF, result);
		super.affectStatusDC(Command.SUBWF, super.getArguments());
		
		result &= 0xFF; // result maskieren
		
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
