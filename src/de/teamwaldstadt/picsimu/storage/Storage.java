package de.teamwaldstadt.picsimu.storage;

import java.util.Stack;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.command.Command;
import de.teamwaldstadt.picsimu.utils.Utils;

public class Storage {

	private Stack<Integer> stack;
	private int[] storage;
	private int w, pc;

	public Storage() {
		this.storage = new int[32 * 8]; // PIC main storage of 256 bytes
		this.stack = new Stack<>();
		this.resetAll(); // initialize main storage and w register
	}

	public void resetAll() {
		this.resetStorage();
		this.resetW();
		this.stack.clear();
		this.pc = 0;
	}

	public void resetStorage() {
		for (int i = 0; i < storage.length; i++) {
			this.storage[i] = 0x00;
		}

		this.resetAllRegisters();
	}

	public void resetAllRegisters() {
		for (SpecialRegister register : SpecialRegister.values()) {
			this.resetRegister(register);
		}
	}

	public void resetRegister(SpecialRegister register) {
		this.storage[register.getAddress()] = register.getDefaultValue();

		if (register.getBank() == Bank.ALL) {
			this.storage[register.getAddress() + Bank.OFFSET] = register.getDefaultValue();
		}
	}
	
	public void resetRegister(GeneralRegister register) {
		this.storage[register.getAddress()] = 0x00;
		this.storage[register.getAddress() + Bank.OFFSET] = 0x00;
	}

	public void resetW() {
		this.w = 0x00;
	}
	
	private void manipulatePC(int arg) throws Exception {
		int lower = arg;
		int upper = Utils.extractBitsFromIntNumber(this.getRegister(SpecialRegister.PCLATH.getAddress(), true), 0, 5);
		
		// hier wird absichtlich setRegister(SpecialRegister-Objekt, Integer-Wert) und nicht 
		// setRegister(SpecialRegister-Adresse, Integer-Wert, Boolean-IgnoreBank) verwendet
		this.setRegister(SpecialRegister.PCL, lower);
		
		this.setRegister(SpecialRegister.PCLATH.getAddress(), upper, true);
		
		this.pc = (upper << 8) + lower;
	}
	
	public void jumpPC(int arg) throws Exception {
		Utils.checkBitsExceed(arg, 11);
		
		int lower = arg & 0xFF;
		int upper = (Utils.extractBitsFromIntNumber(this.getRegister(SpecialRegister.PCLATH.getAddress(), true), 3, 2) << 3) + Utils.extractBitsFromIntNumber(arg, 8, 3);
		
		// hier wird absichtlich setRegister(SpecialRegister-Objekt, Integer-Wert) und nicht 
		// setRegister(SpecialRegister-Adresse, Integer-Wert, Boolean-IgnoreBank) verwendet
		this.setRegister(SpecialRegister.PCL, lower);
		
		this.pc = (upper << 8) + lower;
	}
	
	public void incrementPC() throws Exception {
		this.pc = pc + 1;
		
		// hier wird absichtlich setRegister(SpecialRegister-Objekt, Integer-Wert) und nicht 
		// setRegister(SpecialRegister-Adresse, Integer-Wert, Boolean-IgnoreBank) verwendet
		this.setRegister(SpecialRegister.PCL, this.pc & 0xFF);
	}

	public int[] getStorage() {
		return this.storage;
	}
	
	public int getRegister(int address, boolean ignoreBank) throws Exception {
		// indirekte Adressierung
		if (address == 0x00) {
			address = this.getRegister(SpecialRegister.FSR);
		}
		
		// Bank 1
		if (Bank.getCurrent() == Bank.BANK_1 && !ignoreBank) {
			address += Bank.OFFSET;
		}
		
		try {
			SpecialRegister register = SpecialRegister.atAddress(address);
			
			return this.getRegister(register);
		} catch (Exception e) {
			// do nothing
		}
		
		try {
			GeneralRegister register = new GeneralRegister(address);
			
			return this.getRegister(register);
		} catch (Exception e) {
			// do nothing
		}
		
//		throw new Exception("Invalid register address: " + String.format("%2X", address));
		return 0x00; // if no register found, return 0x00
	}

	private int getRegister(SpecialRegister register) {
		return this.storage[register.getAddress()];
	}

	private int getRegister(GeneralRegister register) {
		return this.storage[register.getAddress()];
	}

	public int getW() {
		return this.w;
	}
	
	public Stack<Integer> getStack() {
		return this.stack;
	}
	
	public int getPC() {
		return this.pc;
	}
	
	public boolean isBitOfRegisterSet(int register, int bitIndex, boolean ignoreBank) throws Exception {
		if (bitIndex < 0 || bitIndex > 7) {
			throw new Exception("Out of byte (digit mismatch): " + bitIndex);
		}
		
		int value = this.getRegister(register, ignoreBank);
		
		return ((value >> (bitIndex)) & 1) == 1;
	}
	
	public void setRegister(int address, int value, boolean ignoreBank) throws Exception {
		Utils.checkBitsExceed(value, 8);
		
		// indirekte Adressierung
		if (address == 0x00) {
			address = this.getRegister(SpecialRegister.FSR);
		}
		
		// Bank 1
		if (Bank.getCurrent() == Bank.BANK_1 && !ignoreBank) {
			address += Bank.OFFSET;
		}
		
		try {
			SpecialRegister register = SpecialRegister.atAddress(address);
			
			// Sonderfall f�r PCL
			if (register == SpecialRegister.PCL) {
				this.manipulatePC(value);
				return;
			}
			if (register == SpecialRegister.TMR0) {
				//see page 27 on Datenblatt
				CodeExecutor.prescalerTact = -1; 
			}
			if (register == SpecialRegister.PORTA) {
				int currentVal = Main.STORAGE.getRegister(register);
				int option = Main.STORAGE.getRegister(SpecialRegister.OPTION_REG);
				boolean tose = (option & 16) == 0 ? false: true; 
				if (tose) {
					//falling edge
					if ((currentVal & 16) != 0) {
						if ((value & 16) == 0 && (option & 32) != 0) {
							Main.EXECUTOR.triggerTMR0(option);
						}
					}
				} else {
					//raising edge
					if ((currentVal & 16) == 0) {
						if ((value & 16) != 0 && (option & 32) != 0) {
							Main.EXECUTOR.triggerTMR0(option);
						}
					}
				}
			}
			
			this.setRegister(register, value);
			return;
		} catch (Exception e) {
			// do nothing
		}
		
		try {
			GeneralRegister register = new GeneralRegister(address);
			
			this.setRegister(register, value);
			return;
		} catch (Exception e) {
			// do nothing
		}
		
		throw new Exception("Invalid register address: " + String.format("%2X", address));
	}

	public void setTimer(int value) throws Exception {
		setRegister(SpecialRegister.TMR0, value);
	}
	
	private void setRegister(SpecialRegister register, int value) throws Exception {
		Utils.checkBitsExceed(value, 8);
		
		this.storage[register.getAddress()] = value;

		if (register.getBank() == Bank.ALL) {
			this.storage[register.getAddress() + Bank.OFFSET] = value;
		}
	}

	private void setRegister(GeneralRegister register, int value) throws Exception {
		Utils.checkBitsExceed(value, 8);
		this.storage[register.getAddress()] = value;
		this.storage[register.getAddress() + Bank.OFFSET] = value;
	}

	public void setW(int w) throws Exception {
		Utils.checkBitsExceed(w, 8);
		this.w = w;
	}
	
	public void setBitOfRegister(int register, int bitIndex, boolean setBit, boolean ignoreBank) throws Exception {
		if (bitIndex < 0 || bitIndex > 7) {
			throw new Exception("Out of byte (digit mismatch): " + bitIndex);
		}
		
		int value = this.getRegister(register, ignoreBank);
		
		if (setBit) {
			value |= (1 << bitIndex);
		} else {
			value &= ~(1 << bitIndex);
		}
		
		this.setRegister(register, value, ignoreBank);
	}

}
