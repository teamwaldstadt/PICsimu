package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.utils.Utils;

public class CommandSUBWF extends CommandExecutor {
	
	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandSUBWF(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.isDestinationBitSet = Utils.extractBitsFromIntNumber(arguments, 7, 1) == 1;
		this.fileRegister = Utils.extractBitsFromIntNumber(arguments, 0, 7);
	}

	@Override
	public void execute() throws Exception {
		int w = ((Main.STORAGE.getW() ^ 0xFF) + 1) & 0xFF; // 2er-Komplement und maskieren
		int f = Main.STORAGE.getRegister(this.fileRegister, false);
		int result = f + w;
		
		super.affectStatus(Command.SUBWF, result);
		super.affectStatusDC(Command.SUBWF, super.getArguments());
		
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
