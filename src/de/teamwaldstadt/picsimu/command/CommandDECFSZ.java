package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.utils.Utils;

public class CommandDECFSZ extends CommandExecutor {
	
	private boolean isDestinationBitSet;
	private int fileRegister;

	public CommandDECFSZ(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.isDestinationBitSet = Utils.extractBitsFromIntNumber(arguments, 7, 1) == 1;
		this.fileRegister = Utils.extractBitsFromIntNumber(arguments, 0, 7);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		int f = Main.STORAGE.getRegister(this.fileRegister, false);
		int minus = ((~1) + 1) & 0xFF; // 2er-Komplement und maskieren
		int result = f + minus;
		
		super.affectStatus(Command.DECFSZ, result);
		
		result &= 0xFF; // result maskieren
		
		if (this.isDestinationBitSet) {
			Main.STORAGE.setRegister(this.fileRegister, result, false);
		} else {
			Main.STORAGE.setW(result);
		}
		
		// wenn result 0x00, dann f�hre NOP aus und erh�he Runtime-Counter
		if (result == 0x00) {
			new CommandNOP(super.getArguments()).execute();
			Main.EXECUTOR.incrementRuntime(Command.NOP);
			
			System.out.println("Command '" + Command.DECFSZ.name() + "' skipped the next command");
		}
	}
	
	public boolean isDestinationBitSet() {
		return this.isDestinationBitSet;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
