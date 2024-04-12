package vn.com.booking.utils;

import vn.com.booking.models.Account;

import java.util.regex.Pattern;

public class RegexUtil {
	public static String usernameRegex = "^[a-zA-Z0-9_]{4,30}$";

	public static String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-`~!@#$%^&*()_+={}[\\\\]|:;\\\"'<>,.?/]).{8,30}$";

	public static String phoneNumberRegex = "^\\d{10}$";

	public static String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	public static String ageRegex = "^(1[8-9]|[2-9][0-9])$";

	public static Boolean checkRegex(String regex, String value) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value).matches();
	}

	public static Boolean checkLogin(String username, String password) {
		return checkRegex(usernameRegex, username)
				&& checkRegex(passwordRegex, password);
	}

	public static boolean checkProfileRegex(Account profile) {
		return RegexUtil.checkRegex(RegexUtil.phoneNumberRegex, profile.getPhoneNumber())
				&& RegexUtil.checkRegex(RegexUtil.ageRegex, profile.getAge().toString());

	}
}