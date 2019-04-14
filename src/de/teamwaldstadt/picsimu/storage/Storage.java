package de.teamwaldstadt.picsimu.storage;

import de.teamwaldstadt.picsimu.utils.Utils;

public class Storage {

	private int[] storage;
	private int w;

	public Storage() {
		this.storage = new int[32 * 8]; // PIC main storage of 256 bytes
		this.resetAll(); // initialize main storage and w register
	}

	public void resetAll() {
		this.resetStorage();
		this.resetW();
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
	
	public int getPC() throws Exception {
		int low = this.getRegister(SpecialRegister.PCL);
		int high = Utils.extractBitsFromIntNumber(this.getRegister(SpecialRegister.PCLATH), 0, 5);
		
		String lowSequence = Integer.toBinaryString(low);
		String highSequence = Integer.toBinaryString(high);
		
		while (lowSequence.length() < 8) {
			lowSequence = "0" + lowSequence;
		}
		
		while (highSequence.length() < 8) {
			highSequence = "0" + highSequence;
		}
		
		return Integer.valueOf(highSequence + lowSequence, 2);
	}
	
	public boolean isBitOfRegisterSet(int register, int bitIndex, boolean ignoreBank) throws Exception {
		if (bitIndex < 0 || bitIndex > 7) {
			throw new Exception("Out of byte (digit mismatch): " + bitIndex);
		}
		
		int value = this.getRegister(register, ignoreBank);
		
		return ((value >> (bitIndex)) & 1) == 1;
	}

	public void setStorage(int[] storage) throws Exception {
		if (storage.length != this.storage.length) {
			throw new Exception("Storage size mismatch (found: " + storage.length + ", expected: " + this.storage.length + ")");
		}

		this.storage = storage;
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
			this.setRegister(SpecialRegister.atAddress(address), value);
			return;
		} catch (Exception e) {
			// do nothing
		}
		
		try {			
			this.setRegister(new GeneralRegister(address), value);
			return;
		} catch (Exception e) {
			// do nothing
		}
		
		throw new Exception("Invalid register address: " + String.format("%2X", address));
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
	
	public void setPC(int pc) throws Exception {
		Utils.checkBitsExceed(pc, 13);
		
		int low = Utils.extractBitsFromIntNumber(pc, 0, 8);
		int high = Utils.extractBitsFromIntNumber(pc, 8, 5);
		
		this.setRegister(SpecialRegister.PCL, low);
		this.setRegister(SpecialRegister.PCLATH, high);
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
