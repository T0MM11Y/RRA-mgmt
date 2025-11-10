package com.twm.mgmt.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



public class StringUtilsEx extends StringUtils {
	
	
	public static boolean hasUniqueSymbol(String str) {
		if (isBlank(str)) {

			return true;
		}

		String regex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(str);

		return matcher.find();
	}

	public static String replaceLineSeparator(String str) {

		return replaceAll(str, "[\\r\\n\\t]", "");
	}

	public static String replaceUniqueSymbol(String str) {

		return replaceAll(str, "[~!@#%^&*()_=+\\[\\]\\\\{}|;:'\"?/.,><$\\r\\n\\t/r/n/t-。∞]", "");
	}

	public static boolean isNumeric(String str) {
		str = subZeroAndDot(str);
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0 ?$", "");
			s = s.replaceAll("[.]$", "");
		}
		return s;
	}

	public static String replaceAll(String str, String regex, String replacement) {
		if (isBlank(str) || isBlank(regex)) {

			return str;
		}

		return str.replaceAll(regex, replacement);
	}

	public static String bytes2HexString(byte[] bytes) {
		if (bytes != null) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < bytes.length; i++) {
				if (i > 10) {
					break;
				}

				sb.append(String.format("%02X", bytes[i]));
			}

			return sb.toString();
		}

		return "";
	}
	
	public static String[] getEnglish (String[] str) {
		List<String> listString = new ArrayList<String>();
		
		for (int i = 0; i < str.length; i++) {
			listString.add(str[i].replaceAll("[^(A-Za-z)]", ""));
		}
		
		str = listString.toArray(new String[listString.size()]);
		return str;
	}

	public static boolean isCampaignApplySpeed(String str) {

		String[] arr = new String[] { "4G", "5G", "4G+5G" };
		return Arrays.asList(arr).contains(str);

	}

	public static boolean isCampaignApplyChannel(String[] str) {

		String[] arr = new String[] { "SB", "SC", "SL", "SK", "SH", "SD", "SA", "SF", "SJ", "SG" };

		AtomicInteger a = new AtomicInteger(str.length);

		Arrays.stream(arr).forEach(s -> {

			if (Arrays.stream(str).anyMatch(t -> t.contains(s))) {
				a.getAndDecrement();
			}

		});

		return a.get() == 0;
	}

	public static boolean isCampaignApplyType(String[] str) {

		String[] arr = new String[] { "AQ", "NP", "RT" };

		AtomicInteger a = new AtomicInteger(str.length);

		Arrays.stream(arr).forEach(s -> {

			if (Arrays.stream(str).anyMatch(t -> t.matches(s))) {
				a.getAndDecrement();
			}

		});

		return a.get() == 0;
	}

	public static boolean isProjectPartNo(String str) {

		return str.matches("^[A-Z].*?");
		
	}

	public static boolean isProjectAttributes(String str) {
//		
//		str = subZeroAndDot(str);
//
//		String[] arr = new String[] { "656", "668", "675", "651", "652", "672", "01", "673", "303", "597" };
//		return Arrays.asList(arr).contains(str);
		return isNumeric(str);

	}



	public static boolean isIsCheckMobileData(String[] str) {

		String[] arr = new String[] { "A", "B", "C", "D", "E", "F" };

		AtomicInteger a = new AtomicInteger(str.length);

		Arrays.stream(arr).forEach(s -> {

			if (Arrays.stream(str).anyMatch(t -> t.contains(s))) {
				a.getAndDecrement();
			}

		});

		return a.get() == 0;
	}
		
	public static boolean isProjectCode(String str) {

		return str.matches("[A-Z]{2}[0-9]{3}");
	}
	
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		
		if (StringUtils.isBlank(str)) {
			return b;
		}
		
		p = Pattern.compile("^09\\d{8}$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
	
	public static String getCodeDescription(String code) {
		switch (code) {
		case "SB":
			return "SB 直營";
		case "SC":
			return "SC 加盟";
		case "SG":
			return "SG 網路門市";
		case "SK":
			return "SK 客服";
		case "SL":
			return "SL 電銷";
		case "SD":
			return "SD 直銷";
		case "SA":
			return "SA 區域經銷";
		case "SF":
			return "SF 專案經銷";
		case "SJ":
			return "SJ 台灣電店-經銷";
		default:
			return "未知的代碼";
		}
		
	}
	
	public static String filenameEncode(String name) {
		try {
			return java.net.URLEncoder.encode(name,"utf-8").replace("+","%20");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return name;
		}
	}

}
