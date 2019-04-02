package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.storage.Register;
import de.teamwaldstadt.picsimu.storage.Status;
import de.teamwaldstadt.picsimu.storage.Storage;

public class CommandADDLW extends CommandExecutor {
	
	int literal;
	
	public CommandADDLW(Storage storageAffected, int literal) throws Exception {
		super.setStorageAffected(storageAffected);
		Storage.checkNotAByte(literal);
		
		this.literal = literal;
	}

	@Override
	public void execute() throws Exception {
		int w = super.getStorageAffected().getW();
		int result = w + literal;
		
		boolean carry = false;
		boolean digitCarry = false;
		boolean zBit = false;
		
		// TODO wann wird DC genau gesetzt? so wie unten oder
		// reicht es, wenn w nach der Operation > 0x0F ist?
		// TODO wird C auf 1 gesetzt, wenn w nach der Operation > 0x0F ist?
		if (w <= 0x0F && result > 0x0F) {
			digitCarry = true;
		}
		
		// TODO wird C auf 0 gesetzt, wenn w nach der Operation > 0xFF ist?
		if (result > 0xFF) {
			result = 0xFF;
			carry = true;
		}
		
		if (result == 0x00) {
			zBit = true;
		}
		
		super.getStorageAffected().setW(result);
		super.getStorageAffected().setBitOfRegister(Register.STATUS, Status.CARRY.getBitDigit(), carry);
		super.getStorageAffected().setBitOfRegister(Register.STATUS, Status.DIGIT_CARRY.getBitDigit(), digitCarry);
		super.getStorageAffected().setBitOfRegister(Register.STATUS, Status.ZERO_BIT.getBitDigit(), zBit);
	}
	
	public int getLiteral() {
		return this.literal;
	}

}
