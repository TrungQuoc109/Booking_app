package vn.com.booking.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.booking.Repository.AccountRepository;
import vn.com.booking.Repository.ProfileRepository;
import vn.com.booking.models.Account;
import vn.com.booking.models.Profile;
import vn.com.booking.utils.JwtUtil;
import vn.com.booking.utils.RegexUtil;

import java.util.HashMap;
import java.util.Map;

@Service
@Data
public class UserService {

	@Value("${jwt.type}")
	private String tokenType;

	private AccountRepository accountRepository;
	private ProfileRepository profileRepository;
	Map<String, Object> res = null;

	JwtUtil jwtUtil = new JwtUtil();

	private Map<String, Object> LoginResponse(String message,
	                                          String tokenvalue, Integer accountId, Integer role) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("accountId", accountId);
		response.put("role", role);
		Map<String, Object> token = new HashMap<>();
		token.put("value", tokenvalue);
		token.put("type", tokenType);
		response.put("token", token);
		response.put("expiresIn", jwtUtil.jwtExpirationMs);
		return response;
	}

	private Map<String,Object> ProfileResponse(Profile profile){
		Map<String, Object> response = new HashMap<>();
		response.put("name",profile.getName());
		response.put("age",profile.getAge());
		response.put("numberPhone",profile.getNumberPhone());
		response.put("emergencyContact",profile.getEmergencyContact());
		response.put("email",profile.getEmail());
		return  response;
	}

	private Map<String, Object> MessageResponse(String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return response;
	}


	@Autowired
	public UserService(AccountRepository accountRepository, ProfileRepository profileRepository) {
		this.accountRepository = accountRepository;
		this.profileRepository = profileRepository;
	}

	public ResponseEntity<Map<String, Object>> Login(Account accountReq) {
		try {

			if (accountReq.getUsername() == null || accountReq.getPassword() == null) {
				res = this.MessageResponse("Invalid username or password");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}

			if (!RegexUtil.checkRegex(RegexUtil.usernameRegex, accountReq.getUsername())
					|| !RegexUtil.checkRegex(RegexUtil.passwordRegex, accountReq.getPassword())) {
				res = this.MessageResponse("Invalid username or password");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}

			Account acc = accountRepository.findByUsername((accountReq.getUsername()));
			if (acc != null) {
				if (acc.getPassword().equals(accountReq.getPassword())) {
					String token = jwtUtil.generateJwtToken(acc);
					res = this.LoginResponse("Login successful", token, acc.getAccountId(), acc.getRole());
					return ResponseEntity.status(HttpStatus.OK).body(res);
				} else {
					res = this.MessageResponse("Incorrect password or username");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
				}

			}
			res = this.MessageResponse("Incorrect password or username");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		} catch (Exception error) {
			System.err.print(error.getMessage());
			res = this.MessageResponse("Internal Server Error" + error.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	public ResponseEntity<Map<String, Object>> Register(Map<String, Object> info) {

		try {
			Account acc = new Account();
			Profile profile = new Profile();

			//Check Input data
			if (info == null) {
				res = this.MessageResponse("Not sent data");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
			if (info.get("username") == null || info.get("password") == null || info.get("numberPhone") == null ||
					info.get("emergencyContact") == null || info.get("age") == null || info.get("name") == null) {
				res = this.MessageResponse("Missing data");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}

			acc.setUsername(info.get("username").toString());
			acc.setPassword(info.get("password").toString());

			//Check regex account
			if (!RegexUtil.checkRegex(RegexUtil.usernameRegex, acc.getUsername())
					|| !RegexUtil.checkRegex(RegexUtil.passwordRegex, acc.getPassword())) {
				res = this.MessageResponse("Invalid username or password");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}


			profile.setNumberPhone(info.get("numberPhone").toString());
			profile.setEmergencyContact(info.get("emergencyContact").toString());
			profile.setAge(Integer.parseInt(info.get("age").toString()));
			profile.setName(info.get("name").toString());
			if(info.get("email") !=null) {
				profile.setEmail(info.get("email").toString());
			}

			//Check regex profile
			if (!RegexUtil.checkRegex(RegexUtil.phoneNumberRegex, profile.getNumberPhone())
					|| !RegexUtil.checkRegex(RegexUtil.phoneNumberRegex, profile.getEmergencyContact())
					|| !RegexUtil.checkRegex(RegexUtil.ageRegex, profile.getAge().toString())) {
				res = this.MessageResponse("Invalid value profile");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
			if (accountRepository.findByUsername(acc.getUsername()) != null) {
				res = this.MessageResponse("Username Already exits");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
			Account savedAccount = accountRepository.save(acc);
			profile.setAccount(savedAccount);
			Profile savedProfile = profileRepository.save(profile);
			res = this.MessageResponse("Registration successful");
			return ResponseEntity.status(HttpStatus.OK).body(res);

		} catch (Exception error) {
			System.err.print(error.getMessage());
			res = this.MessageResponse("Internal Server Error" + error.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}
	public  ResponseEntity<Map<String, Object>> GetProfile(Integer accountId) {
		try {
			if (accountId == null) {
				res = this.MessageResponse("Missing data");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
			Profile profile = profileRepository.findByAccount_AccountId(accountId);
			if(profile== null){
				res = this.MessageResponse("Profile not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
			}
			res = this.ProfileResponse(profile);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
		catch (Exception error)
		{
			System.err.print(error.getMessage());
			res = this.MessageResponse("Internal Server Error" +error.getMessage() );
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}
}