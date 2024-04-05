package vn.com.booking.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.com.booking.models.Account;
import vn.com.booking.models.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Response {
	public ResponseEntity<Map<String, Object>> MessageResponse(String message, HttpStatus status) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}
	public ResponseEntity<Map<String, Object>> LoginResponse(String tokenvalue, String tokenType, Integer jwtExpirationMs
																		, Account account, HttpStatus status) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Login successful");
		response.put("accountId", account.getAccountId());
		response.put("role", account.getRole());
		Map<String, Object> token = new HashMap<>();
		token.put("value", tokenvalue);
		token.put("type", tokenType);
		response.put("token", token);
		response.put("expiresIn", jwtExpirationMs);
		return ResponseEntity.status(status).body(response);
	}

	public   ResponseEntity<Map<String, Object>> ProfileResponse(Profile profile,HttpStatus status){
		Map<String, Object> response = new HashMap<>();
		response.put("name",profile.getName());
		response.put("age",profile.getAge());
		response.put("numberPhone",profile.getPhoneNumber());
		response.put("emergencyContact",profile.getEmergencyContact());
		response.put("email",profile.getEmail());
		return ResponseEntity.status(status).body(response);
	}
	public ResponseEntity <List<Object>> ListProfileResponse(List<Profile> profiles,HttpStatus status) {
		List<Object> response = new ArrayList<>();
		for (Profile profile : profiles){
			Map<String,Object> ele = new HashMap<>();
			ele.put("name",profile.getName());
			ele.put("id",profile.getAccount().getAccountId());
			ele.put("age",profile.getAge());
			ele.put("numberPhone",profile.getPhoneNumber());
			response.add(ele);
		}
		return ResponseEntity.status(status).body(response);
	}

}
