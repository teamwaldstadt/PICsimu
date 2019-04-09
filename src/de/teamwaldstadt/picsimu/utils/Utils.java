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
}
