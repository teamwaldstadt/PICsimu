package de.teamwaldstadt.picsimu.storage;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.utils.Utils;

public enum Bank {

	BANK_0, BANK_1, ALL;

	public static int OFFSET = 0x80;

	public static Bank getCurrent() throws Exception {
		/*
		 * Hier wird explizit nicht die Funktion Main.STORAGE isBitOfRegisterSet(...)
		 * verwendet, da dies zu einer Endlosrekursion f�hrt!
		 */
		int rp0 = Utils.extractBitsFromIntNumber(Main.STORAGE.getStorage()[SpecialRegister.STATUS.getAddress()], Status.RP0.getBitIndex(), 1);

		return rp0 == 1 ? BANK_1 : BANK_0;
	}

}
