package de.teamwaldstadt.picsimu.command;

public class CommandNOP extends CommandExecutor {
	
	public CommandNOP(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		// No operation
		
		super.affectStatus(Command.NOP, 0);
		super.incrementPC();
	}

}
