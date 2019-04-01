package de.teamwaldstadt.picsimu.command;

public abstract class Command {
	
	/*
	 * Command super class
	 */
	
	private Command next;
	
	public Command() {}
	
	public abstract void execute();
	
	public void next() {
		if (this.next == null) {
			System.out.println("Done!");
			return;
		}
		
		this.next.execute();
	}
	
	public Command getNext() {
		return this.next;
	}
	
	public void setNext(Command next) {
		this.next = next;
	}

}
