package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.utils.Utils;

public class CommandBTFSC extends CommandExecutor {
	
	private int bitIndex;
	private int fileRegister;

	public CommandBTFSC(int arguments) throws Exception {
		super.setArguments(arguments);
		
		this.bitIndex = Utils.extractBitsFromIntNumber(arguments, 7, 3);
		this.fileRegister = Utils.extractBitsFromIntNumber(arguments, 0, 7);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		boolean testBit = Main.STORAGE.isBitOfRegisterSet(this.fileRegister, this.bitIndex, false);
		
		super.affectStatus(Command.BTFSC, 0);
		
		// wenn testBit nicht gesetzt, dann fuehre NOP aus und erhoehe Runtime-Counter
		if (!testBit) {
			new CommandNOP(super.getArguments()).execute();
			Main.EXECUTOR.incrementRuntime(Command.NOP);
			
			System.out.println("Command '" + Command.BTFSC.name() + "' skipped the next command");
		}
	}
	
	public int getBitIndex() {
		return this.bitIndex;
	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
