package atdd.path.web;

import atdd.path.application.LoginService;
import atdd.path.application.dto.AccessTokenResponseView;
import atdd.path.application.dto.CreateAccessTokenRequestView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	private LoginService loginService;

	public LoginController(final LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping(value = "/oauth/token")
	public ResponseEntity login(@RequestBody CreateAccessTokenRequestView request) {
		AccessTokenResponseView response = loginService.createToken(request);
		return ResponseEntity.ok(response);
	}
}
