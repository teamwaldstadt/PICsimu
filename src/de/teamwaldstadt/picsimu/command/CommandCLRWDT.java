package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class CommandCLRWDT extends CommandExecutor {
	
	public CommandCLRWDT(int arguments) throws Exception {
		super.setArguments(arguments);
	}

	@Override
	public void execute() throws Exception {
		Main.STORAGE.incrementPC();
		
		// set Prescaler Assignment (PSA) bit
		Main.STORAGE.setBitOfRegister(SpecialRegister.OPTION_REG.getAddress(), 3, true, false);
		
		// PSA is assigned to WDT, so the Prescaler Rate Select bits are set to '000' (= 1:1 rate)
		Main.STORAGE.setBitOfRegister(SpecialRegister.OPTION_REG.getAddress(), 0, false, false);
		Main.STORAGE.setBitOfRegister(SpecialRegister.OPTION_REG.getAddress(), 1, false, false);
		Main.STORAGE.setBitOfRegister(SpecialRegister.OPTION_REG.getAddress(), 2, false, false);
		
		// clear the wdt
		CodeExecutor.watchdogCounter = 0;
		
		// affect TO_INV and PD_INV
		super.affectStatus(Command.CLRWDT, 0);
	}

}
