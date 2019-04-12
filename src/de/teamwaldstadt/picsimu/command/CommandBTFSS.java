package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandBTFSS extends CommandExecutor {
	
	private int bitIndex;
	private int fileRegister;

	public CommandBTFSS(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.bitIndex = Storage.extractBitsFromIntNumber(arguments, 2, 5, 12);
		this.fileRegister = Storage.extractBitsFromIntNumber(arguments, 5, 12, 12);
	}

	@Override
	public void execute() throws Exception {
		boolean testBit = Main.STORAGE.isBitOfRegisterSet(this.fileRegister, this.bitIndex, false);
		
		super.affectStatus(Command.BTFSS, 0);
		
		// wenn testBit gesetzt, dann führe NOP aus und erhöhe Runtime-Counter
		if (testBit) {
			new CommandNOP(super.getArguments()).execute();
			Main.EXECUTOR.incrementRuntime(Command.NOP);
			
			System.out.println("Command '" + Command.BTFSS.name() + "' skipped the next command");
		}
		
		super.incrementPC();
	}
	
	public int getBitIndex() {
		return this.bitIndex;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
