package de.teamwaldstadt.picsimu.storage;

public class GeneralRegister {

	public static final int FIRST_ADDRESS = 0x0C;
	public static final int LAST_ADDRESS = 0x4F;

	private int address;

	public GeneralRegister(int address) throws Exception {
		if (address < FIRST_ADDRESS || address > LAST_ADDRESS) {
			throw new Exception("Not a general purpose register (out of range: " + String.format("%2X", FIRST_ADDRESS) + " to " + String.format("%2X", LAST_ADDRESS) + "): " + String.format("%2X", address));
		}

		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}

}
