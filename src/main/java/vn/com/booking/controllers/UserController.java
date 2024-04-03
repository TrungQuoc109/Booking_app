package vn.com.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import vn.com.booking.models.Account;
import vn.com.booking.services.UserService;
import vn.com.booking.utils.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/${apiVersion}/user")
public class UserController {
	private final UserService userService;
	@Autowired

	public UserController(UserService service) {
		this.userService = service;
	}

	JwtUtil jwtUtil = new JwtUtil();
	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<?> Login(@RequestBody Account account){
		return userService.Login(account);
	}
	@PostMapping("/sign-up")
	@ResponseBody
	public ResponseEntity<?> Register(@RequestBody Map<String,Object> info){return  userService.Register(info);}
	@GetMapping("/get-profile")
	@ResponseBody
	public ResponseEntity<?>GetProfile(@RequestHeader("Authorization") String token) {
		Integer accountId;
		if (!jwtUtil.validateJwtToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{message:\"Unauthorized access: Invalid token\"");
		}

		System.out.print(jwtUtil.getAccountIdFromJwtToken(token));
			accountId = jwtUtil.getAccountIdFromJwtToken(token);

			if(accountId == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{message:\"Unauthorized access: Invalid Id\"}");
			}
			return userService.GetProfile(accountId);
	}
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
	}
}
