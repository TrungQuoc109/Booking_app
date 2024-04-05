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
import vn.com.booking.response.Response;
import vn.com.booking.utils.JwtUtil;
import vn.com.booking.utils.RegexUtil;

import java.util.Map;

@Service
@Data
public class UserService {

	@Value("${jwt.type}")
	private String tokenType;

	private AccountRepository accountRepository;
	private ProfileRepository profileRepository;
	private Response response;
	Map<String, Object> res = null;

	JwtUtil jwtUtil ;








	@Autowired
	public UserService(AccountRepository accountRepository, ProfileRepository profileRepository) {
		this.accountRepository = accountRepository;
		this.profileRepository = profileRepository;
		this.response = new Response();
		jwtUtil = new JwtUtil();
	}

	public ResponseEntity<Map<String, Object>> Login(Account accountReq) {
		try {

			if (accountReq.getUsername() == null || accountReq.getPassword() == null||
					!RegexUtil.checkRegex(RegexUtil.usernameRegex, accountReq.getUsername())
					|| !RegexUtil.checkRegex(RegexUtil.passwordRegex, accountReq.getPassword())) {
				return  this.response.MessageResponse("Invalid username or password",HttpStatus.BAD_REQUEST);
			}

			Account acc = accountRepository.findByUsername((accountReq.getUsername()));
			if (acc != null) {
				if (acc.getPassword().equals(accountReq.getPassword())) {
					String token = jwtUtil.generateJwtToken(acc);
					return this.response.LoginResponse(token,tokenType,jwtUtil.jwtExpirationMs,acc,HttpStatus.OK);
				} else {
					return this.response.MessageResponse("Incorrect password or username",HttpStatus.BAD_REQUEST);
				}

			}
			return this.response.MessageResponse("Incorrect password or username",HttpStatus.BAD_REQUEST);

		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	public  ResponseEntity<Map<String, Object>> GetProfile(Integer accountId) {
		try {
			if (accountId == null) {
				return this.response.MessageResponse("Missing data",HttpStatus.BAD_REQUEST);
			}
			Profile profile = profileRepository.findByAccount_AccountId(accountId);
			if(profile== null){
				return this.response.MessageResponse("Profile not found!",HttpStatus.NOT_FOUND);
			}
			return this.response.ProfileResponse(profile,HttpStatus.OK);
		}
		catch (Exception error)
		{
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}