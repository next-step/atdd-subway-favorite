package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	private final AuthService authService;

	public LoginController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login/token")
	public ResponseEntity<TokenResponse> getAccessToken(@RequestBody TokenRequest tokenRequest) {
		return ResponseEntity.ok().body(authService.createToken(tokenRequest));
	}

	@PostMapping("/login/github")
	public ResponseEntity<TokenResponse> getGithubAccessToken(@RequestBody GithubTokenRequest githubTokenRequest) {
		return ResponseEntity.ok().body(authService.createGitHubToken(githubTokenRequest));
	}
}
