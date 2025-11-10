package com.twm.mgmt.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomUtil {
	private static final String NUMBERS = "0123456789";

	private static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";

	private static final String UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

//	private static final String OTHER_CHARS = "!@#$%-+?";

	private static final String ALL_CHARS = NUMBERS + LOWER_CHARS + UPPER_CHARS;

	public static String genRandom(int len) {
		StringBuilder sb = new StringBuilder();

		List<String> prefixlist = Arrays.asList("0", "1", "2");
		Collections.shuffle(prefixlist);

		for (int i = 0; i < len; i++) {
			if (prefixlist.size() > i) {
				String prefix = prefixlist.get(i);
				switch (prefix) {
				case "0":
					sb.append(NUMBERS.charAt((int) (Math.random() * NUMBERS.length())));
					break;
				case "1":
					sb.append(LOWER_CHARS.charAt((int) (Math.random() * LOWER_CHARS.length())));
					break;
				case "2":
					sb.append(UPPER_CHARS.charAt((int) (Math.random() * UPPER_CHARS.length())));
					break;
				}
			} else {
				sb.append(ALL_CHARS.charAt((int) (Math.random() * ALL_CHARS.length())));
			}
		}
		return sb.toString();
	}

}
