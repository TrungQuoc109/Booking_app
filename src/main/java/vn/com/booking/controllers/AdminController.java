package vn.com.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.booking.services.AdminService;
import vn.com.booking.services.UserService;
import vn.com.booking.utils.JwtUtil;

@RestController
@RequestMapping("/${apiVersion}/admin")
public class AdminController {
	private final AdminService adminService;
	private final UserService userService;
	JwtUtil jwtUtil;
	@Autowired

	public AdminController(AdminService adminService, UserService userService) {

		this.adminService = adminService;
		this.userService = userService;
		jwtUtil = new JwtUtil();
	}



	private  ResponseEntity<?>  checkToken(String token){
		if (!jwtUtil.validateJwtToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{message:\"Unauthorized access: Invalid token\"");
		}
		Integer accountIdFromJwtToken = jwtUtil.getAccountIdFromJwtToken(token);
		Integer role = jwtUtil.getRoleFromJwtToken(token);
		if (accountIdFromJwtToken == null || role == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{message:\"Unauthorized access: Invalid data\"");
		}
		if (role != 0) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
		}
		return  null;
	}


	@GetMapping("/get-users")
	@ResponseBody
	public  ResponseEntity<?> GetUsers(@RequestHeader("Authorization") String token){
		if(checkToken(token) == null) {
			return adminService.GetListUser();
		}
		return checkToken(token);
	}

	@GetMapping("/get-profile/{accountId}")
	@ResponseBody
	public ResponseEntity<?> GetProfileByAdmin(@RequestHeader("Authorization") String token, @PathVariable Integer accountId) {
		if(checkToken(token) == null) {
			return userService.GetProfile(accountId);
		}
		return checkToken(token);

	}
}
