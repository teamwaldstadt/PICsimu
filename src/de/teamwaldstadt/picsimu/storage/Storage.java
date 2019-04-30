package de.teamwaldstadt.picsimu.storage;

import java.util.Stack;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
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
		
		//Spezialfall EECON2:
		if (address == SpecialRegister.EECON2.getAddress())
			return 0x00;
		
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
	
	boolean writeOperationAllowed = false;
	boolean writeStart = false;
	boolean eecon2WasSetToAA = false;
	boolean eecon2WasSetTo55 = false;
	private int[] eeprom = new int[64]; 
	
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
			
			// Sonderfall fuer PCL
			if (register == SpecialRegister.PCL) {
				this.manipulatePC(value);
				return;
			}
			
			
			 
			//Sonderfall für EEPROM
			switch (register) {
			case EECON1: 
				//control register, bits are: EEIF, WRERR, WREN, WR, RD
				int currentVal = Main.STORAGE.getRegister(register);
				//if WR or RD have been set:
				if ((((value & 1) == 0) && ((currentVal & 1) != 0)) || (((value & 2) == 0) && ((currentVal & 2)) != 0)) {
					//if somebody wants to unset them -> do nothing and return
					throw new Exception("Write not allowed"); 
				}	
				//if WREN has been set:
				if ((currentVal & 4) == 0 && (value & 4) != 0) {
					writeOperationAllowed = true;
				} else if ((currentVal & 4) != 0 && (value & 4) == 0) {
					writeOperationAllowed = false;
				}
				
				//TODO: 
				//if WR has been set:
				if ((currentVal & 2) == 0 && (value & 2) != 0) {
					writeStart = true;
				} else if ((currentVal & 2) != 0 && (value & 2) == 0) {
					writeStart = false;
				}
				
					
				
				if ((currentVal & 1) == 0 && (value & 1) != 0) {
					doEEPROMRead();
					return;
				}
				
				break;
			case EECON2:
				//not a real physical register
				if (value == 0x55) {
					eecon2WasSetTo55 = true;
				} else if (value == 0xAA && eecon2WasSetTo55) {
					eecon2WasSetTo55 = false;
					eecon2WasSetToAA = true;
				}
			case EEDATA: 
				//nothing special about this, is like a normal register
				break;
			case EEADR: 
				value &= 0x3f;
				break;
			default: break;
			}
			
			if (writeStart && writeOperationAllowed && eecon2WasSetToAA) {
				doEEPROMWrite();
				return;
			}
			
			if (register == SpecialRegister.TMR0) {
				CodeExecutor.prescalerTact = 1; 
			}
			
			if (register == SpecialRegister.PORTB) {
				int currentVal = Main.STORAGE.getRegister(register);
				int option = Main.STORAGE.getRegister(SpecialRegister.OPTION_REG);
				
				boolean intedge = (option & 64) == 0 ? true: false;
				if (intedge) {
					//falling edge
					if ((currentVal & 1) != 0) {
						if ((value & 1) == 0) {
							setBitOfRegister(SpecialRegister.INTCON.getAddress(), 1, true, true);
						}
					}
				} else {
					//raising edge
					if ((currentVal & 1) == 0) {
						if ((value & 1) != 0) {
							setBitOfRegister(SpecialRegister.INTCON.getAddress(), 1, true, true);
						}
					}
				}
				
				//TODO: set RBIF by change in one of RB4-RB7, has to be input
				int mask = getRegister(SpecialRegister.TRISB.getAddress(), true);
				if (((currentVal & mask) & 0xF0) != ((value & mask) & 0xF0)) {
					setBitOfRegister(SpecialRegister.INTCON.getAddress(), 0, true, false);
				}
			}
			
			if (register == SpecialRegister.PORTA) {
				int currentVal = Main.STORAGE.getRegister(register);
				int option = Main.STORAGE.getRegister(SpecialRegister.OPTION_REG);
				boolean tose = (option & 16) == 0 ? false: true;
				if (tose) {
					//falling edge
					if ((currentVal & 16) != 0) {
						if ((value & 16) == 0 && (option & 32) != 0) {
							Main.EXECUTOR.triggerTMR0();
						}
					}
				} else {
					//raising edge
					if ((currentVal & 16) == 0) {
						if ((value & 16) != 0 && (option & 32) != 0) {
							Main.EXECUTOR.triggerTMR0();
						}
					}
				}
			}
			
			this.setRegister(register, value);
			return;
		} catch (Exception e) {
			if (e.getMessage().contentEquals("Write not allowed")) {
				throw e;
			}
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
	
	private void doEEPROMWrite() throws Exception {

		eecon2WasSetToAA = false;
		writeStart = false;
		
		eeprom[getRegister(SpecialRegister.EEADR.getAddress(), true)] = getRegister(SpecialRegister.EEDATA.getAddress(), true);
		
		//set EEIF if finshed
		Main.STORAGE.setBitOfRegister(SpecialRegister.EECON1.getAddress(), 4, true, true);
		
		//reset WR
		int newEECON1 = Main.STORAGE.getRegister(SpecialRegister.EECON1.getAddress(), true);
		newEECON1 &= 0xFD;
		Main.STORAGE.setRegister(SpecialRegister.EECON1, newEECON1);
		
		System.out.println("EEPROM WRITE executed successfully");
	}
	
	private void doEEPROMRead() throws Exception {
		Main.STORAGE.setRegister(SpecialRegister.EEDATA.getAddress(), eeprom[getRegister(SpecialRegister.EEADR.getAddress(), true)], true);
		System.out.println("EEPROM READ executed successfully");
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
