package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.storage.Storage;

public abstract class CommandExecutor {

	private CommandExecutor next;
	private Storage storageAffected;

	public CommandExecutor() {
	}

	public abstract void execute() throws Exception;

	public void next() throws Exception {
		if (this.next == null) {
			System.out.println("Done!");
			return;
		}

		this.next.execute();
	}

	public CommandExecutor getNext() {
		return this.next;
	}
	
	public Storage getStorageAffected() {
		return this.storageAffected;
	}

	public void setNext(CommandExecutor next) {
		this.next = next;
	}
	
	public void setStorageAffected(Storage storageAffected) {
		this.storageAffected = storageAffected;
	}

}
