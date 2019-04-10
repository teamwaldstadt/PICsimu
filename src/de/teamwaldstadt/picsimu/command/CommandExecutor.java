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
	
	// TODO for RP0, RP1, IRP
	public void affectStatus(Command command, int result) throws Exception {
		List<Status> statusAffected = Arrays.asList(command.getStatusAffected());
		
		if (statusAffected.contains(Status.C)) {
			boolean carry = false;
			
			if (result > 0xFF) {
				carry = true;
			}
			
			Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), Status.C.getBitIndex(), carry, true);
		}
		
		if (statusAffected.contains(Status.Z)) {
			boolean zBit = false;
			
			if ((result & 0xFF) == 0x00) {
				zBit = true;
			}
		
			Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), Status.Z.getBitIndex(), zBit, true);
		}
		
		if (statusAffected.contains(Status.PD_INV)) {
			if (command == Command.CLRWDT) {
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), Status.PD_INV.getBitIndex(), true, true);
			} else if (command == Command.SLEEP) {
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), Status.PD_INV.getBitIndex(), false, true);
			}
		}
		
		if (statusAffected.contains(Status.TO_INV)) {
			if (command == Command.CLRWDT || command == Command.SLEEP) {
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), Status.TO_INV.getBitIndex(), true, true);
			}
		}
	}
	
	public void affectStatusDC(Command command, int argument) throws Exception {
		List<Status> statusAffected = Arrays.asList(command.getStatusAffected());
		
		if (!statusAffected.contains(Status.DC)) {
			return;
		}
		
		boolean digitCarry = false;

		int arg = argument & 0x0F;
		int w = Main.STORAGE.getW() & 0x0F;
		
		if ((command == Command.ADDLW || command == Command.ADDWF) && arg + w > 0x0F) {
			digitCarry = true;
		} else if ((command == Command.SUBLW || command == Command.SUBWF) && arg - w < 0x0F) {
			digitCarry = true;
		}

		Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), Status.DC.getBitIndex(), digitCarry, true);
	}
	
	public void incrementPC() throws Exception {
		Main.STORAGE.setPC(Main.STORAGE.getPC() + 1);
	}
	
	public int getArguments() {
		return this.arguments;
	}
	
	public void setArguments(int arguments) throws Exception {
		Storage.check12Bits(arguments);
		
		this.arguments = arguments;
	}

}
