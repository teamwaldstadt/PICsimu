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
		int f = Main.STORAGE.getRegister(this.fileRegister, false);
		int result = f - 1;
		
		// TODO wie verhält sich der Command, wenn f bereits 0 ist?
		if (result == -1) {
			result = 0x00;
		}
		
		super.affectStatus(Command.DECFSZ, result);
		
		result &= 0xFF; // result maskieren
		
		if (this.isDestinationBitSet) {
			Main.STORAGE.setRegister(this.fileRegister, result, false);
		} else {
			Main.STORAGE.setW(result);
		}
		
		// wenn result 0x00, dann führe NOP aus und erhöhe Runtime-Counter
		if (result == 0x00) {
			new CommandNOP(super.getArguments()).execute();
			Main.EXECUTOR.incrementRuntime(Command.NOP);
			
			System.out.println("Command '" + Command.DECFSZ.name() + "' skipped the next command");
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
