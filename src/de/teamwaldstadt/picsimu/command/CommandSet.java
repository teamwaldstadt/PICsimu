package de.teamwaldstadt.picsimu.command;

public class CommandSet {
	
	// Command set contains the type of command, its position, its line number and the argument
	
	Command command;
	int commandNr;
	int lineNr;
	int argument;
	
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
}
