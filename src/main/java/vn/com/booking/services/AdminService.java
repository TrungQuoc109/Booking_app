package vn.com.booking.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.booking.Repository.AccountRepository;
import vn.com.booking.Repository.PermissionRepository;
import vn.com.booking.Repository.RoleRepository;
import vn.com.booking.enumclass.PermissionEnum;
import vn.com.booking.enumclass.RoleEnum;
import vn.com.booking.helper.Response;
import vn.com.booking.helper.Validate;
import vn.com.booking.models.Account;
import vn.com.booking.models.RolePermission;
import vn.com.booking.utils.JwtUtil;
import vn.com.booking.utils.RegexUtil;

import java.lang.reflect.Field;
import java.util.*;

@Service
@Data
public class AdminService {

	private static final String[] FIRST_NAMES = {"Văn", "Ngọc", "Thị", "Minh", "Hoàng", "Trung", "Thành", "Thái", "Trúc", "Yến"};
	private static final String[] LAST_NAMES = {"Trần", "Lê", "Nguyễn", "Phạm", "Hoàng", "Dương", "Võ", "Tô", "Lý", "Phan"};
	private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com"};
	private static final Random random = new Random();


	private AccountRepository accountRepository;
	private PermissionRepository permissionRepository;
	private RoleRepository roleRepository;
	private Response response;
	JwtUtil jwtUtil;

	@Autowired
	public AdminService(AccountRepository accountRepository, PermissionRepository permissionRepository, RoleRepository roleRepository) {

		this.accountRepository = accountRepository;
		this.permissionRepository = permissionRepository;
		this.roleRepository = roleRepository;
		this.response = new Response();
		jwtUtil = new JwtUtil(this.response);
	}


	public ResponseEntity<Map<String, Object>> Register(Map<String, Object> info, UUID role) {
		try {
			RolePermission perrmissionIsExisted = permissionRepository.findRolePermissionByRole_RoleIdAndPermissionPermissionName(role, String.valueOf(PermissionEnum.sign_up));
			if (perrmissionIsExisted == null) {
				return this.response.MessageResponse("Unauthorized", HttpStatus.UNAUTHORIZED);
			}

			if (Validate.isDataMissing(info)) {
				return this.response.MessageResponse("Missing data", HttpStatus.BAD_REQUEST);
			}

			Account accountRegister = new Account(info.get("username").toString(), info.get("password").toString());
			if (!Validate.isValidAccount(accountRegister)) {
				return this.response.MessageResponse("Invalid username or password", HttpStatus.BAD_REQUEST);
			}
			accountRegister.setPhoneNumber(info.get("phoneNumber").toString());
			accountRegister.setAge(Integer.parseInt(info.get("age").toString()));
			accountRegister.setName(info.get("name").toString());
			String email = (String) info.get("email");
			if (email != null && !RegexUtil.checkRegex(RegexUtil.emailRegex, email)) {
				return this.response.MessageResponse("Invalid email value", HttpStatus.BAD_REQUEST);
			}
			accountRegister.setEmail(email);

			if (!Validate.isValidProfile(accountRegister)) {
				return this.response.MessageResponse("Invalid profile data", HttpStatus.BAD_REQUEST);
			}
			Account accountIsExisted = accountRepository.findByUsername(accountRegister.getUsername());

			if (accountIsExisted != null) {
				return this.response.MessageResponse("Username Already exits", HttpStatus.BAD_REQUEST);
			}

			if (accountRepository.findByPhoneNumber(accountRegister.getPhoneNumber()) != null) {
				return this.response.MessageResponse("Phone number Already exits", HttpStatus.BAD_REQUEST);
			}
			accountRegister.setRole(roleRepository.findByRoleName(RoleEnum.user.toString()));
			accountRepository.save(accountRegister);

			return this.response.MessageResponse("Registration successful", HttpStatus.CREATED);
		} catch (NumberFormatException error) {
			return this.response.MessageResponse("Invalid age format", HttpStatus.BAD_REQUEST);
		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> GetListUser(UUID role, Integer start, Integer end, String sort, String order) {
		try {
			RolePermission perrmissionIsExist = permissionRepository.findRolePermissionByRole_RoleIdAndPermissionPermissionName(role, PermissionEnum.view_list_user.toString());
			if (perrmissionIsExist == null) {
				return this.response.MessageResponse("Unauthorized", HttpStatus.UNAUTHORIZED);
			}
			Field[] fields = Account.class.getDeclaredFields();
			boolean isPropertyExist = Arrays.stream(fields)
					.anyMatch(field -> field.getName().equals(sort));
			if (!isPropertyExist) {
				return this.response.MessageResponse("Column " + sort + " dose not exist in table ", HttpStatus.BAD_REQUEST);
			}
			if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
				return this.response.MessageResponse("Invalid sorting direction", HttpStatus.BAD_REQUEST);
			}
			Pageable pageable = PageRequest.of(start, end, Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sort));
			List<Account> profiles = accountRepository.findAccountsByRoleRoleName(RoleEnum.user.toString(), pageable);

			if (profiles == null) {
				return this.response.MessageResponse("Profile not found!", HttpStatus.NOT_FOUND);
			}
			return this.response.ListProfileResponse(profiles, start, HttpStatus.OK);
		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error " + error.getMessage() + sort + " " + order + " " + start.toString() + end.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> GenerateData() {
		try {
			for (int i = 1; i <= 1000; i++) {
				Account accountRegister = new Account();
				accountRegister.setUsername("testten" + i);
				accountRegister.setPassword("12345678Chin@");
				accountRegister.setName(generateRandomName());
				accountRegister.setAge(generateRandomAge());
				accountRegister.setPhoneNumber(generateRandomPhoneNumber());
				while (accountRepository.findByPhoneNumber(accountRegister.getPhoneNumber()) != null) {
					accountRegister.setPhoneNumber(generateRandomPhoneNumber());
				}
				accountRegister.setEmail(generateRandomEmail());
				accountRegister.setRole(roleRepository.findByRoleName(RoleEnum.user.toString()));
				Account savedAccount = accountRepository.save(accountRegister);
			}
			return this.response.MessageResponse("Registration successful", HttpStatus.OK);
		} catch (Exception error) {
			System.err.print(error.getMessage());
			return this.response.MessageResponse("Internal Server Error " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	private static String generateRandomName() {
		String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
		String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
		return firstName + " " + lastName;
	}

	// Generate a random age between 18 and 80
	private static int generateRandomAge() {
		return random.nextInt(63) + 18;
	}

	// Generate a random phone number
	private static String generateRandomPhoneNumber() {
		StringBuilder phoneNumber = new StringBuilder();
		phoneNumber.append("0");
		for (int i = 0; i < 9; i++) {
			phoneNumber.append(random.nextInt(10));
		}
		return phoneNumber.toString();
	}

	// Generate a random email
	private static String generateRandomEmail() {
		String domain = DOMAINS[random.nextInt(DOMAINS.length)];
		return generateRandomString(8) + "@" + domain;
	}

	// Generate a random string of given length
	private static String generateRandomString(int length) {
		StringBuilder stringBuilder = new StringBuilder();
		String characters = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < length; i++) {
			stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
		}
		return stringBuilder.toString();
	}

}
