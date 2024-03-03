package nextstep.auth.utils.controller;

import nextstep.auth.application.dto.GithubAuthRequest;
import nextstep.auth.application.dto.GithubAuthResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.utils.fixture.GithubAuthFixture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
	@PostMapping("/github/login/oauth/access_token")
	public ResponseEntity<GithubAuthResponse> getAccessToken(@RequestBody GithubAuthRequest request) {
		String accessToken = GithubAuthFixture.getAccessTokenByCode(request.getCode());
		GithubAuthResponse response = new GithubAuthResponse(accessToken, "", "");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/github/user")
	public ResponseEntity<GithubProfileResponse> getProfile(@RequestHeader("Authorization") String authorization) {
		GithubProfileResponse response = new GithubProfileResponse(GithubAuthFixture.getEmailByCode(authorization));
		return ResponseEntity.ok(response);
	}
}
