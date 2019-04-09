package de.teamwaldstadt.picsimu.storage;

import de.teamwaldstadt.picsimu.Main;

public enum Bank {

	BANK_0, BANK_1, ALL;

	public static int BEGIN_OF_BANK_1 = 0x80;

	public static Bank getCurrent() throws Exception {
		/*
		 * Hier wird explizit nicht die Funktion Main.STORAGE isBitOfRegisterSet(...)
		 * verwendet, da dies zu einer Endlosrekursion führt!
		 */
		int rp0 = Storage.extractBitsFromIntNumber(Main.STORAGE.getStorage()[SpecialRegister.STATUS.getAddress()],
				8 - Status.RP0.getBitIndex() - 1, 8 - Status.RP0.getBitIndex(), 8);

		return rp0 == 1 ? BANK_1 : BANK_0;
	}

}
