package de.teamwaldstadt.picsimu.command;

import java.util.Arrays;
import java.util.List;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;
import de.teamwaldstadt.picsimu.storage.Status;
import de.teamwaldstadt.picsimu.storage.Storage;

public abstract class CommandExecutor {

	private int arguments;

	public CommandExecutor() {
	}

	public abstract void execute() throws Exception;
	
	// TODO for PD_INV, TO_INV, RP0, RP1, IRP
	public void affectStatus(Command command, int result) throws Exception {
		List<Status> statusAffected = Arrays.asList(command.getStatusAffected());
		
		if (statusAffected.contains(Status.C)) {
			boolean carry = false;
			
			// TODO wird C auf 0 gesetzt, wenn result > 0xFF ist?
			if (result > 0xFF) {
				result = 0xFF;
				carry = true;
			}
			
			Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS, Status.DC.getBitIndex(), carry);
		}
		
		if (statusAffected.contains(Status.DC)) {
			boolean digitCarry = false;
			
			// TODO wann wird DC genau gesetzt?
			// TODO reicht es, wenn result > 0x0F ist?
			// TODO wird C auf 1 gesetzt, wenn result > 0x0F ist?
			if (result > 0x0F) {
				digitCarry = true;
			}
	
			Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS, Status.C.getBitIndex(), digitCarry);
		}
		
		if (statusAffected.contains(Status.Z)) {
			boolean zBit = false;
			
			if (result == 0x00) {
				zBit = true;
			}
		
			Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS, Status.Z.getBitIndex(), zBit);
		}
	}
	
	public int getArguments() {
		return this.arguments;
	}
	
	public void setArguments(int arguments) throws Exception {
		Storage.check12Bits(arguments);
		
		this.arguments = arguments;
	}

}
