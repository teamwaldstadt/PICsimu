package de.teamwaldstadt.picsimu.command;

public abstract class CommandExecutor {

	private CommandExecutor next;

	public CommandExecutor() {
	}

	public abstract void execute();

	public void next() {
		if (this.next == null) {
			System.out.println("Done!");
			return;
		}

		this.next.execute();
	}

	public CommandExecutor getNext() {
		return this.next;
	}

	public void setNext(CommandExecutor next) {
		this.next = next;
	}

}
