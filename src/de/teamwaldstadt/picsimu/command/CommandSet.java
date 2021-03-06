package de.teamwaldstadt.picsimu.command;

public class CommandSet {
	
	// Command set contains the type of command, its position, its line number and the argument
	
	private Command command;
	private int commandNr;
	private int lineNr;
	private int argument;
	
	public CommandSet(Command command, int argument, int commandNr, int lineNr) {
		this.command = command;
		this.argument = argument;
		this.commandNr = commandNr;
		this.lineNr = lineNr;
	}
	
	public int getArgument() {
		return this.argument;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public int getCommandNr() {
		return this.commandNr;
	}
	
	public int getLineNr() {
		return this.lineNr - 1;
	}
	
}
