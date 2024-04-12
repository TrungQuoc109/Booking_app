package vn.com.booking.helper;

import vn.com.booking.models.Account;
import vn.com.booking.utils.RegexUtil;

import java.util.Map;

public class Validate {
	public static boolean isValidProfile(Account profile) {
		return RegexUtil.checkProfileRegex(profile);
	}

	public static boolean isValidAccount(Account acc) {
		return RegexUtil.checkLogin(acc.getUsername(), acc.getPassword());
	}

	public static boolean isDataMissing(Map<String, Object> info) {
		return info == null || info.get("username") == null || info.get("password") == null || info.get("phoneNumber") == null ||
				info.get("age") == null || info.get("name") == null;
	}
}
