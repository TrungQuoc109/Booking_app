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


	@GetMapping({"/get-users/{start}/{end}/{sort}/{order}", "/get-users/{start}/{end}",
			"/get-users"})
	@ResponseBody
	public ResponseEntity<?> GetUsers(@RequestHeader("Authorization") String token,
	                                  @PathVariable(required = false) Integer start,
	                                  @PathVariable(required = false) Integer end,
	                                  @PathVariable(required = false) String sort,
	                                  @PathVariable(required = false) String order) {
		if (jwtUtil.checkToken(token) != null) {
			return jwtUtil.checkToken(token);
		}

		start = (start == null) ? 0 : start;
		end = (end == null) ? 50 : end;
		sort = (sort == null) ? "accountId" : sort;
		order = (order == null) ? "asc" : order;
		UUID role = jwtUtil.getRoleFromJwtToken(token);
		return adminService.GetListUser(role, start, end, sort, order);
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
