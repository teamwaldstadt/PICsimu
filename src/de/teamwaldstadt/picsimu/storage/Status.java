package de.teamwaldstadt.picsimu.storage;

public enum Status {
	
	CARRY(0), DIGIT_CARRY(1), ZERO_BIT(2), POWER_DOWN_INV(3), TIME_OUT_INV(4), RP0(5), RP1(6), IRP(7);
	
	private int bitDigit;
	
	Status(int bitDigit) {
		this.bitDigit = bitDigit;
	}
	
	public int getBitDigit() {
		return this.bitDigit;
	}

}
