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
		this.storage[register.getBytePosition()] = register.getDefaultValue();
		;

		if (register.getBank() == Bank.ALL) {
			this.storage[register.getBytePosition() + Bank.BEGIN_OF_BANK_1] = register.getDefaultValue();
		}
	}

	public void resetW() {
		this.w = 0x00;
	}

	public static void checkNotAByte(int testByte) throws Exception {
		if (testByte < 0x00 || testByte > 0xFF) {
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

	// hässlicher Workaround
	// TODO schönere Variante implementieren
	public boolean isBitOfRegisterSet(Register register, int bitDigit) throws Exception {
		if (bitDigit < 0 || bitDigit > 7) {
			throw new Exception("Out of byte (digit mismatch)");
		}

		String bitSequence = String.valueOf(this.getRegister(register));
		char[] bits = bitSequence.toCharArray();
		int realDigit = bitSequence.length() - bitDigit - 1; // Assembler beginnt von rechts bei 0!

		if (bits[realDigit] == 1) {
			return true;
		} else if (bits[realDigit] == 0) {
			return false;
		} else {
			throw new Exception("Invalid bit sequence (not only ones and zeroes)");
		}
	}

	public void setStorage(int[] storage) throws Exception {
		if (storage.length != this.storage.length) {
			throw new Exception("Storage size mismatch");
		}

		this.storage = storage;
	}

	public void setRegister(Register register, int value) throws Exception {
		checkNotAByte(value);
		this.storage[register.getBytePosition()] = value;

		if (register.getBank() == Bank.ALL) {
			this.storage[register.getBytePosition() + Bank.BEGIN_OF_BANK_1] = value;
		}
	}

	public void setW(int w) throws Exception {
		checkNotAByte(w);
		this.w = w;
	}

	// hässlicher Workaround
	// TODO schönere Variante implementieren
	public void setBitOfRegister(Register register, int bitDigit, boolean setBit) throws Exception {
		if (bitDigit < 0 || bitDigit > 7) {
			throw new Exception("Out of byte (digit mismatch)");
		}

		String bitSequence = String.valueOf(this.getRegister(register));
		char[] bits = bitSequence.toCharArray();
		int realDigit = bitSequence.length() - bitDigit - 1; // Assembler beginnt von rechts bei 0!

		bits[realDigit] = setBit ? '1' : '0';
		bitSequence = new String(bits);
		this.setRegister(register, Integer.valueOf(bitSequence));
	}

}
