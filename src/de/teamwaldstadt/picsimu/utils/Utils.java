package de.teamwaldstadt.picsimu.utils;

public class Utils {
	
	public static boolean isValidByte(String value) {
		return value.matches("[0-9a-fA-F]{1,2}");
	}
	
	public static boolean isValidByte(int value) {
		return String.format("%02X", value).matches("[0-9a-fA-F]{1,2}");
	}
	
	public static void log(Object classname, String message) {
		System.out.println("[" + classname.getClass().getSimpleName() + "] " + message);
	}
	
	public static int extractBitsFromIntNumber(int number, int index, int amount) throws Exception {
		if (index < 0) {
			throw new Exception("Out of range: index must be positive or zero (index: " + index + ")");
		}
	
		if (amount <= 0) {
			throw new Exception("Out of range: amount must be positive (amount: " + amount + ")");
		}
		
		return (((1 << amount) - 1) & (number >> index));
	}
	
	public static void checkBitsExceed(int bitSequence, int length) throws Exception {
		int max = (int) Math.pow(2, length) - 1;
		
		if (bitSequence < 0x00 || bitSequence > max) {
			throw new Exception("Exceeded " + length + " bits (out of range: 0x00 to 0x" + String.format("%2X", max) + "): 0x" + String.format("%2X", bitSequence));
		}
	}
	
}
