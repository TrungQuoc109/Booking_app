package vn.com.booking.helper;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.booking.models.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class Response {
	public ResponseEntity<Map<String, Object>> MessageResponse(String message, HttpStatus status) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}

	public ResponseEntity<Map<String, Object>> LoginResponse(String tokenvalue, String tokenType
			, Account account, HttpStatus status) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Login successful");
		response.put("accountId", account.getAccountId());
		response.put("role", account.getRole().getRoleName());
		Map<String, Object> token = new HashMap<>();
		token.put("value", tokenvalue);
		token.put("type", tokenType);
		response.put("token", token);
		return ResponseEntity.status(status).body(response);
	}

	public ResponseEntity<Map<String, Object>> ProfileResponse(Account profile, HttpStatus status) {
		Map<String, Object> response = new HashMap<>();
		response.put("name", profile.getName());
		response.put("age", profile.getAge());
		response.put("phoneNumber", profile.getPhoneNumber());
		response.put("email", profile.getEmail());
		return ResponseEntity.status(status).body(response);
	}

	public ResponseEntity<Map<String, Object>> ListProfileResponse(List<Account> profiles, HttpStatus status) {
		List<Object> data = new ArrayList<>();
		int count = 0;
		for (Account profile : profiles) {
			count++;
			Map<String, Object> ele = new HashMap<>();
			ele.put("name", profile.getName());
			ele.put("id", profile.getAccountId());
			ele.put("age", profile.getAge());
			ele.put("phoneNumber", profile.getPhoneNumber());
			data.add(ele);
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("total", count);
		response.put("page", 0);
		response.put("data", data);
		return ResponseEntity.status(status).body(response);
	}

}
