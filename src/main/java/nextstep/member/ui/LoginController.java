package nextstep.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.LoginService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;

@RestController
public class LoginController {

	private LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping("/login/token")
	public ResponseEntity<TokenResponse> createMember(@RequestBody TokenRequest request) {
		TokenResponse token = loginService.createToken(request);
		return ResponseEntity.ok(token);
	}
}
