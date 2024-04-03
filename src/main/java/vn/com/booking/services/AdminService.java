package vn.com.booking.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.booking.Repository.ProfileRepository;
import vn.com.booking.models.Profile;
import vn.com.booking.utils.JwtUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
public class AdminService {
	private ProfileRepository profileRepository;
	Map<String, Object> res = null;

	JwtUtil jwtUtil = new JwtUtil();
	@Autowired
	public AdminService(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}
	private List<Object> ListProfileResponse(List<Profile> profiles) {
		List<Object> response = new ArrayList<>();
		for (Profile profile : profiles){
			Map<String,Object> ele = new HashMap<>();
			ele.put("name",profile.getName());
			ele.put("id",profile.getAccount().getAccountId());
			ele.put("numberPhone",profile.getNumberPhone());
			response.add(ele);
		}
		return response;
	}
	private Map<String, Object> MessageResponse(String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return response;
	}
	public ResponseEntity<?> GetListUser() {
		try {

			List <Profile> profiles = profileRepository.findAllByAccount_Role(1);
			if(profiles == null){
				res = this.MessageResponse("Profile not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
			}
			return ResponseEntity.status(HttpStatus.OK).body(this.ListProfileResponse(profiles));
		}
		catch (Exception error)
		{
			System.err.print(error.getMessage());
			res = this.MessageResponse("Internal Server Error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}
}
