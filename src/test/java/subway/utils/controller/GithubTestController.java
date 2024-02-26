package subway.utils.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.member.GithubAccessTokenRequest;
import subway.dto.member.GithubAccessTokenResponse;
import subway.dto.member.GithubProfileResponse;
import subway.fixture.member.GithubResponses;

@RestController
public class GithubTestController {
	@PostMapping("/github/login/oauth/access_token")
	public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
		String code = tokenRequest.getCode();
		GithubAccessTokenResponse response = GithubResponses.findAccessTokenByCode(code);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/github/user")
	public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
		String accessToken = authorization.split(" ")[1];
		GithubProfileResponse response = GithubResponses.findProfileByAccessToken(accessToken);
		return ResponseEntity.ok(response);
	}
}
