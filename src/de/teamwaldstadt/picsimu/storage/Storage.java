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
			this.storage[register.getAddress() + Bank.BEGIN_OF_BANK_1] = register.getDefaultValue();
		}
	}
	
	public void resetRegister(GeneralRegister register) {
		this.storage[register.getAddress()] = 0x00;
		this.storage[register.getAddress() + Bank.BEGIN_OF_BANK_1] = 0x00;
	}

	public void resetW() {
		this.w = 0x00;
	}

	public static void check8Bits(int bitSequence) throws Exception {
		if (bitSequence < 0x00 || bitSequence > 0xFF) {
			throw new Exception("Not 8 bits (out of range: 0x00 to 0xFF)");
		}
	}

	public static void check7Bits(int bitSequence) throws Exception {
		if (bitSequence < 0x00 || bitSequence > 0x7F) {
			throw new Exception("Not 7 bits (out of range: 0x00 to 0x7F)");
		}
	}
	
	public static void check12Bits(int bitSequence) throws Exception {
		if (bitSequence < 0x00 || bitSequence > 0xFFF) {
			throw new Exception("Not 12 bits (out of range: 0x00 to 0xFFF)");
		}
	}
	
	// hässlicher Workaround
	// TODO schönere Variante implementieren
	public static int extractBitsFromIntNumber(int number, int endIndex, int beginIndex) throws Exception {
		String bitSequence = Integer.toBinaryString(number);
		
		if (beginIndex < 0 || endIndex >= bitSequence.length()) {
			throw new Exception("Out of range");
		}
		
		int realBeginIndex = bitSequence.length() - beginIndex - 1; // Assembler beginnt von rechts bei 0!
		int realEndIndex = bitSequence.length() - endIndex - 1; // Assembler beginnt von rechts bei 0!
		String resultBits = bitSequence.substring(realBeginIndex, realEndIndex);
		
		return Integer.valueOf(resultBits, 2);
	}

	public int[] getStorage() {
		return this.storage;
	}

	public int getRegister(SpecialRegister register) {
		return this.storage[register.getAddress()];
	}

	public int getRegister(GeneralRegister register) {
		return this.storage[register.getAddress()];
	}

	public int getW() {
		return this.w;
	}

	// hässlicher Workaround
	// TODO schönere Variante implementieren
	public boolean isBitOfRegisterSet(SpecialRegister register, int bitDigit) throws Exception {
		if (bitDigit < 0 || bitDigit > 7) {
			throw new Exception("Out of byte (digit mismatch)");
		}

		String bitSequence = Integer.toBinaryString(this.getRegister(register));
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

	// hässlicher Workaround
	// TODO schönere Variante implementieren
	public boolean isBitOfRegisterSet(GeneralRegister register, int bitDigit) throws Exception {
		if (bitDigit < 0 || bitDigit > 7) {
			throw new Exception("Out of byte (digit mismatch)");
		}

		String bitSequence = Integer.toBinaryString(this.getRegister(register));
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

	public void setRegister(SpecialRegister register, int value) throws Exception {
		check8Bits(value);
		this.storage[register.getAddress()] = value;

		if (register.getBank() == Bank.ALL) {
			this.storage[register.getAddress() + Bank.BEGIN_OF_BANK_1] = value;
		}
	}

	public void setRegister(GeneralRegister register, int value) throws Exception {
		check8Bits(value);
		this.storage[register.getAddress()] = value;
		this.storage[register.getAddress() + Bank.BEGIN_OF_BANK_1] = value;
	}

	public void setW(int w) throws Exception {
		check8Bits(w);
		this.w = w;
	}

	// hässlicher Workaround
	// TODO schönere Variante implementieren
	public void setBitOfRegister(SpecialRegister register, int bitDigit, boolean setBit) throws Exception {
		if (bitDigit < 0 || bitDigit > 7) {
			throw new Exception("Out of byte (digit mismatch)");
		}

		String bitSequence = Integer.toBinaryString(this.getRegister(register));
		char[] bits = bitSequence.toCharArray();
		int realDigit = bitSequence.length() - bitDigit - 1; // Assembler beginnt von rechts bei 0!

		bits[realDigit] = setBit ? '1' : '0';
		bitSequence = new String(bits);
		this.setRegister(register, Integer.valueOf(bitSequence, 2));
	}

	// hässlicher Workaround
	// TODO schönere Variante implementieren
	public void setBitOfRegister(GeneralRegister register, int bitDigit, boolean setBit) throws Exception {
		if (bitDigit < 0 || bitDigit > 7) {
			throw new Exception("Out of byte (digit mismatch)");
		}

		String bitSequence = Integer.toBinaryString(this.getRegister(register));
		char[] bits = bitSequence.toCharArray();
		int realDigit = bitSequence.length() - bitDigit - 1; // Assembler beginnt von rechts bei 0!

		bits[realDigit] = setBit ? '1' : '0';
		bitSequence = new String(bits);
		this.setRegister(register, Integer.valueOf(bitSequence, 2));
	}

}
