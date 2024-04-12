package vn.com.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.booking.helper.Response;
import vn.com.booking.services.AdminService;
import vn.com.booking.services.UserService;
import vn.com.booking.utils.JwtUtil;

import java.util.Map;
import java.util.UUID;

@CrossOrigin("${applicationPath}")
@RestController
@RequestMapping("/${apiVersion}/admin")
public class AdminController {
	private final AdminService adminService;

	private final Response response;
	JwtUtil jwtUtil;

	@Autowired

	public AdminController(AdminService adminService, UserService userService) {

		this.adminService = adminService;
		this.response = new Response();
		jwtUtil = new JwtUtil(this.response);
	}





	@GetMapping("/get-users")
	@ResponseBody
	public ResponseEntity<?> GetUsers(@RequestHeader("Authorization") String token) {
		if (jwtUtil.checkToken(token) == null) {
			UUID role = jwtUtil.getRoleFromJwtToken(token);
			return adminService.GetListUser(role);
		}
		return jwtUtil.checkToken(token);
	}

	@GetMapping("/gen")
	@ResponseBody
	public ResponseEntity<?> Gen() {
		return adminService.GenerateData();
	}
	@PostMapping("/sign-up")
	@ResponseBody
	public ResponseEntity<?> Register(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> info) {
		if (jwtUtil.checkToken(token) == null) {
			UUID role = jwtUtil.getRoleFromJwtToken(token);
			return adminService.Register(info, role);
		}
		return jwtUtil.checkToken(token);
	}


}
