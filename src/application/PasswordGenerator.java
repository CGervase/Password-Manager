package application;

import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordGenerator {
	private static final String UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMS = "123456789";
	private static final String SPECIAL_CHARS = "!#$%&()*+,-./:;<=>?@[\\]^_`{|}~";

	public static String genPassword(boolean upper, boolean lower, boolean num, boolean special) {
		SecureRandom r = new SecureRandom();
		int length = 14; // password length is 14
		char[] passChars = new char[14];

		if (upper)
			passChars[getRandIndex(r, passChars)] = UPPER_CHARS.charAt(r.nextInt(UPPER_CHARS.length()));
		if (lower)
			passChars[getRandIndex(r, passChars)] = LOWER_CHARS.charAt(r.nextInt(LOWER_CHARS.length()));
		if (num)
			passChars[getRandIndex(r, passChars)] = NUMS.charAt(r.nextInt(NUMS.length()));
		if (special)
			passChars[getRandIndex(r, passChars)] = SPECIAL_CHARS.charAt(r.nextInt(SPECIAL_CHARS.length()));

		for (int i = 0; i < length; i++) {
			if (passChars[i] == 0) {
				int set = r.nextInt(4);
				switch (set) {
				case 0:
					passChars[i] = UPPER_CHARS.charAt(r.nextInt(UPPER_CHARS.length()));
					break;
				case 1:
					passChars[i] = LOWER_CHARS.charAt(r.nextInt(LOWER_CHARS.length()));
					break;
				case 2:
					passChars[i] = NUMS.charAt(r.nextInt(NUMS.length()));
					break;
				case 3:
					passChars[i] = SPECIAL_CHARS.charAt(r.nextInt(SPECIAL_CHARS.length()));
					break;
				}
			}
		}

		return Arrays.toString(passChars).replace(", ", "").replaceAll("[\\[\\]]", "");
	}

	private static int getRandIndex(SecureRandom r, char[] passChars) {
		int index = r.nextInt(14);
		while (passChars[index] != 0)
			index = r.nextInt(14);

		return index;
	}
}