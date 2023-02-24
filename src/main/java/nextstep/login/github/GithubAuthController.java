package nextstep.login.github;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.auth.Auth;
import nextstep.auth.AuthGithub;

@RestController
@RequiredArgsConstructor
public class GithubAuthController {

	private final GithubAuthService githubAuthService;

	@PostMapping("/login/github")
	public ResponseEntity<GithubAccessTokenResponse> githubAuthLogin(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
		GithubAccessTokenResponse accessToken = githubAuthService.findUserByCode(githubAccessTokenRequest);
		return ResponseEntity.ok().body(accessToken);
	}

	@GetMapping("/login/github")
	public ResponseEntity<GithubProfileResponse> githubAuthLogin(@Auth AuthGithub accessToken) {
		GithubProfileResponse profile = githubAuthService.findUserByToken(accessToken);
		return ResponseEntity.ok().body(profile);
	}
}
