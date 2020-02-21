package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

	private UserService userService;

	public UserController(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users/me")
	public ResponseEntity retrieveMyInfo(HttpServletRequest req) {
		UserResponseView response = userService.retrieveMyInfo(req);
		return ResponseEntity.ok(response);
	}
}
