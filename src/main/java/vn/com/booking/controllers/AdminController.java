package vn.com.booking.controllers;

import vn.com.booking.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.booking.services.AdminService;
import vn.com.booking.services.UserService;
import vn.com.booking.utils.JwtUtil;

import java.util.Map;
@CrossOrigin("${applicationPath}")
@RestController
@RequestMapping("/${apiVersion}/admin")
public class AdminController {
	private final AdminService adminService;
	private final UserService userService;
	private Response response;
	JwtUtil jwtUtil;
	@Autowired

	public AdminController(AdminService adminService, UserService userService) {

		this.adminService = adminService;
		this.userService = userService;
		this.response = new Response();
		jwtUtil = new JwtUtil();
	}



	private  ResponseEntity<?>  checkToken(String token){
		if (!jwtUtil.validateJwtToken(token)) {
			return this.response.MessageResponse("Unauthorized access: Invalid token", HttpStatus.UNAUTHORIZED);
		}
		Integer accountIdFromJwtToken = jwtUtil.getAccountIdFromJwtToken(token);
		Integer role = jwtUtil.getRoleFromJwtToken(token);
		if (accountIdFromJwtToken == null || role == null) {
			return this.response.MessageResponse("Unauthorized access: Invalid data", HttpStatus.UNAUTHORIZED);
		}
		if (role != 0) {
			return this.response.MessageResponse("Forbidden", HttpStatus.FORBIDDEN);
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
	@PostMapping("/sign-up")
	@ResponseBody
	public  ResponseEntity<?> Register(@RequestHeader("Authorization") String token,@RequestBody Map<String,Object> info){
		if(checkToken(token) == null) {
			return adminService.Register(info);
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
