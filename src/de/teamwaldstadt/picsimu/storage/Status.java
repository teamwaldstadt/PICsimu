package de.teamwaldstadt.picsimu.storage;

public enum Status {
	
	C(0), DC(1), Z(2), PD_INV(3), TO_INV(4), RP0(5), RP1(6), IRP(7);
	
	private int bitDigit;
	
	Status(int bitDigit) {
		this.bitDigit = bitDigit;
	}
	
	public int getBitDigit() {
		return this.bitDigit;
	}

}
