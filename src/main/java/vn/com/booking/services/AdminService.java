package vn.com.booking.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.booking.Repository.AccountRepository;
import vn.com.booking.Repository.ProfileRepository;
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
			Account acc = new Account();
			Profile profile = new Profile();
			//Check Input data
			if (info == null) {
				return this.response.MessageResponse("Not sent data",HttpStatus.BAD_REQUEST);
			}
			if (info.get("username") == null || info.get("password") == null || info.get("phoneNumber") == null ||
					info.get("emergencyContact") == null || info.get("age") == null || info.get("name") == null) {
				return this.response.MessageResponse("Missing data",HttpStatus.BAD_REQUEST);
			}
			acc.setUsername(info.get("username").toString());
			acc.setPassword(info.get("password").toString());
			//Check regex account
			if (!RegexUtil.checkRegex(RegexUtil.usernameRegex, acc.getUsername())
					|| !RegexUtil.checkRegex(RegexUtil.passwordRegex, acc.getPassword())) {
				return this.response.MessageResponse("Invalid username or password",HttpStatus.BAD_REQUEST);
			}
			profile.setPhoneNumber(info.get("phoneNumber").toString());
			profile.setEmergencyContact(info.get("emergencyContact").toString());
			profile.setAge(Integer.parseInt(info.get("age").toString()));
			profile.setName(info.get("name").toString());
			if(info.get("email") !=null && RegexUtil.checkRegex(RegexUtil.emailRegex,info.get("email").toString())) {
				profile.setEmail(info.get("email").toString());
			}
			//Check regex profile
			if (!RegexUtil.checkRegex(RegexUtil.phoneNumberRegex, profile.getPhoneNumber())
					|| !RegexUtil.checkRegex(RegexUtil.phoneNumberRegex, profile.getEmergencyContact())
					|| !RegexUtil.checkRegex(RegexUtil.ageRegex, profile.getAge().toString())) {
				return  this.response.MessageResponse("Invalid value profile",HttpStatus.BAD_REQUEST);
			}
			if (accountRepository.findByUsername(acc.getUsername()) != null) {
				return this.response.MessageResponse("Username Already exits",HttpStatus.BAD_REQUEST);
			}
			if(profileRepository.findByPhoneNumberOrEmail(profile.getPhoneNumber(),profile.getEmail())!=null){
			return this.response.MessageResponse("Phone number or Email Already exits",HttpStatus.BAD_REQUEST);
			}
			Account savedAccount = accountRepository.save(acc);
			profile.setAccount(savedAccount);
			Profile savedProfile = profileRepository.save(profile);
			return this.response.MessageResponse("Registration successful",HttpStatus.OK);

		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error" + error.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> GetListUser() {
		try {
			List <Profile> profiles = profileRepository.findAllByAccount_Role(1); // 1 is role of User
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
}
