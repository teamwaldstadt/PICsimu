package de.teamwaldstadt.picsimu.command;

public enum AllCommands {
	ADDWF (0x0700, 8),
	ANDWF (0x0500, 8),
	CLRF (0x0180, 7),
	CLRW (0x0100, 7),
	MOVLW (0x3000, 10);
	
	private int value;
	private int length;
	
	private AllCommands(int value, int length) {
		this.value = value;
		this.length = length;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getLength() {
		return this.length;
	}
}
