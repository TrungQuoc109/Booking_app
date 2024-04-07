package vn.com.booking.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.booking.Repository.AccountRepository;
import vn.com.booking.Repository.ProfileRepository;
import vn.com.booking.enumclass.Role;
import vn.com.booking.models.Account;
import vn.com.booking.models.Profile;
import vn.com.booking.response.Response;
import vn.com.booking.utils.JwtUtil;
import vn.com.booking.utils.RegexUtil;

import java.util.List;
import java.util.Map;

@Service
@Data
public class AdminService {
	private ProfileRepository profileRepository;
	private AccountRepository accountRepository;
	private Response response;
	JwtUtil jwtUtil ;
	@Autowired
	public AdminService(ProfileRepository profileRepository, AccountRepository accountRepository) {
		this.profileRepository = profileRepository;
		this.accountRepository = accountRepository;
		this.response = new Response();
		jwtUtil = new JwtUtil();
	}


	public ResponseEntity<Map<String, Object>> Register(Map<String, Object> info) {
		try {
			if (isDataMissing(info)) {
				return this.response.MessageResponse("Missing data", HttpStatus.BAD_REQUEST);
			}

			Account acc = new Account(info.get("username").toString(),info.get("password").toString());
			if (!isValidAccount(acc)) {
				return this.response.MessageResponse("Invalid username or password", HttpStatus.BAD_REQUEST);
			}

			Profile profile = createProfile(info);
			if (!isValidProfile(profile)) {
				return this.response.MessageResponse("Invalid profile data", HttpStatus.BAD_REQUEST);
			}

			if (accountRepository.findByUsername(acc.getUsername()) != null) {
				return this.response.MessageResponse("Username Already exits", HttpStatus.BAD_REQUEST);
			}

			if (profileRepository.findByPhoneNumber(profile.getPhoneNumber()) != null) {
				return this.response.MessageResponse("Phone number Already exits", HttpStatus.BAD_REQUEST);
			}

			Account savedAccount = accountRepository.save(acc);
			profile.setAccount(savedAccount);
			Profile savedProfile = profileRepository.save(profile);

			return this.response.MessageResponse("Registration successful", HttpStatus.OK);
		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error" + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	public ResponseEntity<?> GetListUser() {
		try {
			List <Profile> profiles = profileRepository.findAllByAccount_Role(Role.User.getValue());
			if(profiles == null){
				return this.response.MessageResponse("Profile not found!",HttpStatus.NOT_FOUND);
			}
			return this.response.ListProfileResponse(profiles,HttpStatus.OK);
		}
		catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	private boolean isDataMissing(Map<String, Object> info) {
		return info == null || info.get("username") == null || info.get("password") == null || info.get("phoneNumber") == null ||
				info.get("emergencyContact") == null || info.get("age") == null || info.get("name") == null;
	}


	private boolean isValidAccount(Account acc) {
		return RegexUtil.checkLogin(acc.getUsername(), acc.getPassword());
	}

	private Profile createProfile(Map<String, Object> info) {
		Profile profile = new Profile();
		profile.setPhoneNumber(info.get("phoneNumber").toString());
		profile.setEmergencyContact(info.get("emergencyContact").toString());
		try {
			profile.setAge(Integer.parseInt(info.get("age").toString()));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid age value");
		}
		profile.setName(info.get("name").toString());
		String email = (String) info.get("email");
		if (email != null && !RegexUtil.checkRegex(RegexUtil.emailRegex, email)) {
			throw new IllegalArgumentException("Invalid email value");
		}
		profile.setEmail(email);
		return profile;
	}

	private boolean isValidProfile(Profile profile) {
		return RegexUtil.checkProfileRegex(profile);
	}



}
