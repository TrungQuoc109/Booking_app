package vn.com.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import vn.com.booking.models.Account;
import vn.com.booking.response.Response;
import vn.com.booking.services.UserService;
import vn.com.booking.utils.JwtUtil;

@CrossOrigin("${applicationPath}")
@RestController
@RequestMapping("/${apiVersion}/user")
public class UserController {
	private final UserService userService;
	private final Response response;
	@Autowired

	public UserController(UserService service) {
		this.userService = service;
		this.response = new Response();
	}

	JwtUtil jwtUtil = new JwtUtil();
	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<?> Login(@RequestBody Account account){
		return userService.Login(account);
	}
	@GetMapping("/get-profile")
	@ResponseBody
	public ResponseEntity<?>GetProfile(@RequestHeader("Authorization") String token) {
		Integer accountId;
		if (!jwtUtil.validateJwtToken(token)) {
			return this.response.MessageResponse("Unauthorized access: Invalid token", HttpStatus.UNAUTHORIZED);
		}
			accountId = jwtUtil.getAccountIdFromJwtToken(token);
			if(accountId == null) {
				return this.response.MessageResponse("Unauthorized access: Invalid id", HttpStatus.UNAUTHORIZED);
			}
			return userService.GetProfile(accountId);
	}
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return this.response.MessageResponse("Invalid request body", HttpStatus.BAD_REQUEST);
	}
}
