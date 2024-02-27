package nextstep.utils.member;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
	@PostMapping("/github/login/oauth/access_token")
	public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
		String accessToken = GithubAuthFixture.getAccessTokenByCode(request.getCode());
		GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/github/user")
	public ResponseEntity<GithubProfileResponse> getProfile(@RequestHeader("Authorization") String authorization) {
		GithubProfileResponse response = new GithubProfileResponse(GithubAuthFixture.getEmailByCode(authorization));
		return ResponseEntity.ok(response);
	}
}
