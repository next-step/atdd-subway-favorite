package nextstep.subway.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;

@RestController
public class FakeGithubController {
	private static final String AUTHENTICATION_TYPE = "token";

	public FakeGithubController() {
	}

	@PostMapping("/github/accessToken")
	public ResponseEntity<GithubAccessTokenResponse> loginWithToken(@RequestBody GithubAccessTokenRequest request) {
		String accessToken = GithubResponses.getAccessTokenFromCode(request.getCode());
		GithubAccessTokenResponse githubAccessToken = new GithubAccessTokenResponse(accessToken);
		return ResponseEntity.ok(githubAccessToken);
	}

	@GetMapping("/github/profile")
	public ResponseEntity<GithubProfileResponse> loginWithToken(
		@RequestHeader(value = "Authorization") String authorization) {
		String token = authorization.replace(String.format("%s ", AUTHENTICATION_TYPE), "");
		String email = GithubResponses.getEmailFromAccessToken(token);

		GithubProfileResponse githubProfile = new GithubProfileResponse(email);
		return ResponseEntity.ok(githubProfile);
	}
}
