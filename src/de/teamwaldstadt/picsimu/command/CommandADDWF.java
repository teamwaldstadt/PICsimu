package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandADDWF extends CommandExecutor {
	
	private int fileRegister;

	public CommandADDWF(Storage storageAffected, int fileRegister) throws Exception {
		super.setStorageAffected(storageAffected);
		Storage.checkNotHalfAByte(fileRegister);
		
		this.fileRegister = fileRegister;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public int getFileRegister() {
		return this.fileRegister;
	}

}
