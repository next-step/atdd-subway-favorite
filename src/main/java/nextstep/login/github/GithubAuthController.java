package nextstep.login.github;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GithubAuthController {

	private final GithubAuthService githubAuthService;

	@PostMapping("/login/github")
	public ResponseEntity<GithubResponse> githubAuthLogin(@RequestBody GithubRequest githubRequest) {
		GithubResponse accessToken = githubAuthService.findUserByCode(githubRequest);
		return ResponseEntity.ok().body(accessToken);
	}
}
