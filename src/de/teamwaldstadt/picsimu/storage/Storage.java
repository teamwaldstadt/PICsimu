package de.teamwaldstadt.picsimu.storage;

public class Storage {
	
	private int[] storage;
	private int w;
	
	public Storage() {
		this.storage = new int[32 * 8]; // PIC main storage of 256 bytes
		this.resetAll(); // initialize main storage and w register
	}
	
	public void resetAll() {
		this.resetStorage();
		this.resetAllRegisters();
		this.resetW();
	}
	
	public void resetStorage() {
		for (int i = 0; i < storage.length; i++) {
			this.storage[i] = 0x00;
		}
	}
	
	public void resetAllRegisters() {
		for (Register register : Register.values()) {
			this.resetRegister(register);
		}
	}
	
	public void resetRegister(Register register) {
		this.storage[register.getBytePosition()] = register.getDefaultValue();;
		
		if (register.getBank() == Bank.ALL) {
			this.storage[register.getBytePosition() + Bank.BEGIN_OF_BANK_1] = register.getDefaultValue();
		}
	}
	
	public void resetW() {
		this.w = 0x00;
	}
	
	private void checkNotAByte(int register) throws Exception {
		if (register < 0x00 || register > 0xFF) {
			throw new Exception("Not a byte (out of range)");
		}
	}
	
	public int[] getStorage() {
		return this.storage;
	}
	
	public int getRegister(Register register) {
		return this.storage[register.getBytePosition()];
	}
	
	public int getW() {
		return this.w;
	}
	
	public void setStorage(int[] storage) throws Exception {
		if (storage.length != this.storage.length) {
			throw new Exception("Storage size mismatch");
		}
		
		this.storage = storage;
	}
	
	public void setRegister(Register register, int value) throws Exception {
		this.checkNotAByte(value);
		this.storage[register.getBytePosition()] = value;
		
		if (register.getBank() == Bank.ALL) {
			this.storage[register.getBytePosition() + Bank.BEGIN_OF_BANK_1] = value;
		}
	}
	
	public void setW(int w) throws Exception {
		this.checkNotAByte(w);
		this.w = w;
	}

}
