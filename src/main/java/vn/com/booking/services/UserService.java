package vn.com.booking.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.booking.Repository.AccountRepository;
import vn.com.booking.Repository.PermissionRepository;
import vn.com.booking.Repository.RoleRepository;
import vn.com.booking.enumclass.PermissionEnum;
import vn.com.booking.enumclass.RoleEnum;
import vn.com.booking.helper.Response;
import vn.com.booking.models.Account;
import vn.com.booking.models.Role;
import vn.com.booking.models.RolePermission;
import vn.com.booking.utils.JwtUtil;
import vn.com.booking.utils.RegexUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Data
public class UserService {

	@Value("${jwt.type}")
	private String tokenType;

	private AccountRepository accountRepository;
	private PermissionRepository permissionRepository;
	private RoleRepository roleRepository;
	private Response response;

	JwtUtil jwtUtil;

	@Autowired
	public UserService(AccountRepository accountRepository, PermissionRepository permissionRepository, RoleRepository roleRepository) {

		this.accountRepository = accountRepository;
		this.permissionRepository = permissionRepository;
		this.roleRepository = roleRepository;
		this.response = new Response();
		jwtUtil = new JwtUtil(this.response);
	}

	public ResponseEntity<Map<String, Object>> Login(Account accountReq) {
		try {

			if (accountReq.getUsername() == null || accountReq.getPassword() == null ||
					!RegexUtil.checkLogin(accountReq.getUsername(), accountReq.getPassword())) {
				return this.response.MessageResponse("Invalid username or password", HttpStatus.BAD_REQUEST);
			}
			Account acc = accountRepository.findByUsername((accountReq.getUsername()));
			if (acc == null || !acc.getPassword().equals(accountReq.getPassword())) {
				return this.response.MessageResponse("Incorrect password or username", HttpStatus.NOT_FOUND);
			}
			String token = jwtUtil.generateJwtToken(acc);
			return this.response.LoginResponse(token, tokenType, acc, HttpStatus.OK);

		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Map<String, Object>> GetProfile(UUID accountId, UUID roleId) {
		try {
			if (checkPermission(roleId, PermissionEnum.view_profile_of_user.toString()) != null) {
				return checkPermission(roleId, PermissionEnum.view_profile_of_user.toString());
			}
			if (accountId == null) {
				return this.response.MessageResponse("Missing Id", HttpStatus.BAD_REQUEST);
			}
			Account profile = accountRepository.findByAccountId(accountId);
			if (profile == null) {
				return this.response.MessageResponse("Profile not found!", HttpStatus.NOT_FOUND);
			}
			return this.response.ProfileResponse(profile, HttpStatus.OK);
		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Map<String, Object>> ChangePassword(UUID accountId, UUID roleId, Map<String, Object> body) {
		try {
			if (checkPermission(roleId, PermissionEnum.change_password.toString()) != null) {
				return checkPermission(roleId, PermissionEnum.change_password.toString());
			}
			if (accountId == null) {
				return this.response.MessageResponse("Missing Id", HttpStatus.BAD_REQUEST);
			}
			Account account = accountRepository.findByAccountId(accountId);
			if (account == null) {
				return this.response.MessageResponse("Account not found", HttpStatus.NOT_FOUND);
			}
			String newPwd = body.get("newPwd").toString();
			String oldPwd = body.get("oldPwd").toString();
			String retypePwd = body.get("retypePwd").toString();

			if (!account.getPassword().equals(oldPwd)) {
				return this.response.MessageResponse("Old password is incorrect.", HttpStatus.BAD_REQUEST);
			}
			if (!newPwd.equals(retypePwd)) {
				return this.response.MessageResponse("New password not equals Retype password.", HttpStatus.BAD_REQUEST);
			}
			account.setPassword(newPwd);
			accountRepository.save(account);
			return response.MessageResponse("Password changed successfully.", HttpStatus.OK);


		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Map<String, Object>> ChangeProfile(UUID accountId, UUID roleId, Map<String, Object> body) {
		try {
			if (checkPermission(roleId, PermissionEnum.change_profile.toString()) != null) {
				return checkPermission(roleId, PermissionEnum.change_profile.toString());
			}
			Account account = accountRepository.findByAccountId(accountId);
			if (account == null) {
				return this.response.MessageResponse("Account not found", HttpStatus.NOT_FOUND);
			}
			if (body.get("profile") == null) {
				return this.response.MessageResponse("Missing Profile", HttpStatus.BAD_REQUEST);
			}
			Map<String, Object> profile = (Map<String, Object>) body.get("profile");
			AtomicBoolean isMissingProfile = new AtomicBoolean(false);
			profile.forEach((key, value) -> {
				if (isMissingProfile.get()) {
					return;
				}
				//chưa kiểm tra data nhập vào là data cũ hay mới
				switch (key) {
					case "name":
						account.setName(value.toString());
						break;
					case "phoneNumber":
						account.setPhoneNumber(value.toString());
						break;
					case "age":
						account.setAge(Integer.parseInt(value.toString()));
						break;
					case "email":
						account.setEmail(value.toString());
						break;
					default:
						isMissingProfile.set(true);
						break;
				}
			});
			if (isMissingProfile.get()) {
				return this.response.MessageResponse("Missing Data", HttpStatus.BAD_REQUEST);
			}
			accountRepository.save(account);
			return response.MessageResponse("Profile changed successfully.", HttpStatus.OK);
		} catch (NumberFormatException error) {
			return this.response.MessageResponse("Invalid age format", HttpStatus.BAD_REQUEST);
		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<Map<String, Object>> checkPermission(UUID roleId, String permisstionValue) {
		Role userRole = roleRepository.findByRoleId(roleId);
		RolePermission isPerrmission = permissionRepository.findRolePermissionByRole_RoleIdAndPermissionPermissionName(roleId, permisstionValue);
		if (!userRole.getRoleName().equals(RoleEnum.user.toString()) && isPerrmission == null) {
			return this.response.MessageResponse("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

		return null;
	}

}