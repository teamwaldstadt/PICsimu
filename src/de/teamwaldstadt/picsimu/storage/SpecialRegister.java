package de.teamwaldstadt.picsimu.storage;

public enum SpecialRegister {

	INDF(0x00, Bank.ALL, 0b00000000),
	PCL(0x02, Bank.ALL, 0b00000000),
	STATUS(0x03, Bank.ALL, 0b00011000),
	FSR(0x04, Bank.ALL, 0b00000000),

	TMR0(0x01, Bank.BANK_0, 0b00000000),
	PORTA(0x05, Bank.BANK_0, 0b00000000),
	PORTB(0x06, Bank.BANK_0, 0b00000000),
	EEDATA(0x08, Bank.BANK_0, 0b00000000),
	EEADR(0x09, Bank.BANK_0, 0b00000000),
	PCLATH(0x0A, Bank.ALL, 0b00000000),
	INTCON(0x0B, Bank.ALL, 0b00000000),

	OPTION_REG(0x81, Bank.BANK_1, 0b11111111),
	TRISA(0x85, Bank.BANK_1, 0b00011111),
	TRISB(0x86, Bank.BANK_1, 0b11111111),
	EECON1(0x88, Bank.BANK_1, 0b00000000),
	EECON2(0x89, Bank.BANK_1, 0b00000000);

	private int address;
	private Bank bank;
	private int defaultValue;

	SpecialRegister(int address, Bank bank, int defaultValue) {
		this.address = address;
		this.bank = bank;
		this.defaultValue = defaultValue;
	}

	public static SpecialRegister atAddress(int address) throws Exception {	
//		if (Bank.getCurrent() == Bank.BANK_1) {
//			address += Bank.BEGIN_OF_BANK_1;
//		}
		
//		if (address < 0x00 || (address >= GeneralRegister.FIRST_ADDRESS && address < Bank.BEGIN_OF_BANK_1)
//				|| address >= (GeneralRegister.FIRST_ADDRESS + Bank.BEGIN_OF_BANK_1)) {
//			throw new Exception("Not a special register (out of range): " + String.format("%2X", address));
//		}
		
		for (SpecialRegister register : values()) {
			if (address == register.getAddress() || (register.getBank() == Bank.ALL
					&& (address == (register.getAddress() + Bank.BEGIN_OF_BANK_1)))) {
				return register;
			}
		}

		throw new Exception("Not a special register (unknown error): " + String.format("%2X", address));
	}

	public int getAddress() {
		return this.address;
	}

	public Bank getBank() {
		return this.bank;
	}

	public int getDefaultValue() {
		return this.defaultValue;
	}

}
