package de.teamwaldstadt.picsimu.storage;

public class GeneralRegister {

	public static final int FIRST_ADDRESS = 0x0C;
	public static final int LAST_ADDRESS = 0x4F;

	private int address;

	public GeneralRegister(int address) throws Exception {
		if (address >= FIRST_ADDRESS && address <= LAST_ADDRESS) {
			this.address = address;
		} else if ((address >= (FIRST_ADDRESS + Bank.BEGIN_OF_BANK_1)) && (address <= (LAST_ADDRESS + Bank.BEGIN_OF_BANK_1))) {
			this.address = address - Bank.BEGIN_OF_BANK_1;
		} else {
			throw new Exception("Not a general purpose register (out of range: " + String.format("%2X", FIRST_ADDRESS)
			+ " to " + String.format("%2X", LAST_ADDRESS) + "): " + String.format("%2X", address));
		}
	}

	public int getAddress() {
		return this.address;
	}

}
