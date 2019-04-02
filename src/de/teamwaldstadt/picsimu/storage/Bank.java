package de.teamwaldstadt.picsimu.storage;

import de.teamwaldstadt.picsimu.Main;

public enum Bank {
	
	BANK_0, BANK_1, ALL;
	
	public static int BEGIN_OF_BANK_1 = 0x80;
	
	public static Bank getCurrent() throws Exception {
		boolean rp0 = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.STATUS, Status.RP0.getBitIndex());
		
		if (rp0) {
			return BANK_1;
		} else {
			return BANK_0;
		}
	}

}
