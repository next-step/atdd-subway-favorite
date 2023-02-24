package nextstep.login.github;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.TokenResponse;

@RestController
@RequiredArgsConstructor
public class GithubAuthController {

	private final GithubAuthService githubAuthService;

	@PostMapping("/login/github")
	public ResponseEntity<TokenResponse> githubAuthLogin(@RequestBody GithubLoginRequest githubLoginRequest) {
		TokenResponse token = githubAuthService.loginByGithub(githubLoginRequest);
		return ResponseEntity.ok().body(token);
	}
}
