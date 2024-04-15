package vn.com.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import vn.com.booking.helper.Response;
import vn.com.booking.models.Account;
import vn.com.booking.services.UserService;
import vn.com.booking.utils.JwtUtil;

import java.util.Map;
import java.util.UUID;

@CrossOrigin("${applicationPath}")
@RestController
@RequestMapping("/${apiVersion}/user")
public class UserController {
	private final UserService userService;
	private final Response response;
	JwtUtil jwtUtil;
	@Autowired

	public UserController(UserService service) {
		this.userService = service;
		this.response = new Response();
		this.jwtUtil = new JwtUtil(this.response);
	}

	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<?> Login(@RequestBody Account account) {
		return userService.Login(account);
	}

	@GetMapping({"/get-profile/{accountId}", "/get-profile"})
	@ResponseBody
	public ResponseEntity<?> GetProfile(@RequestHeader("Authorization") String token, @PathVariable(required = false) UUID accountId) {

		if (jwtUtil.checkToken(token) != null) {
			return jwtUtil.checkToken(token);
		}
		UUID role = jwtUtil.getRoleFromJwtToken(token);
		if (accountId == null) {
			accountId = jwtUtil.getAccountIdFromJwtToken(token);
		}

		return userService.GetProfile(accountId, role);
	}

	@PutMapping({"/change-password/{accountId}", "/change-password"})
	@ResponseBody
	public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @PathVariable(required = false) UUID accountId, @RequestBody Map<String, Object> body) {
		return processRequest(token, accountId, body, "changePassword");
	}

	@PutMapping({"/change-profile/{accountId}", "/change-profile"})
	@ResponseBody
	public ResponseEntity<?> changeProfile(@RequestHeader("Authorization") String token, @PathVariable(required = false) UUID accountId, @RequestBody Map<String, Object> body) {
		return processRequest(token, accountId, body, "changeProfile");
	}

	private ResponseEntity<?> processRequest(String token, UUID accountId, Map<String, Object> body, String action) {
		if (jwtUtil.checkToken(token) != null) {
			return jwtUtil.checkToken(token);
		}
		UUID role = jwtUtil.getRoleFromJwtToken(token);
		if (accountId == null) {
			accountId = jwtUtil.getAccountIdFromJwtToken(token);
		}

		if ("changePassword".equals(action)) {
			return userService.ChangePassword(accountId, role, body);
		}
		if ("changeProfile".equals(action)) {
			return userService.ChangeProfile(accountId, role, body);
		}
		return this.response.MessageResponse("Invalid action", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return this.response.MessageResponse("Invalid request body", HttpStatus.BAD_REQUEST);
	}
}
