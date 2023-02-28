package nextstep.subway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.auth.github.GithubAccessTokenResponse;
import nextstep.auth.github.GithubLoginRequest;
import nextstep.auth.github.GithubProfileResponse;

@RestController
@RequiredArgsConstructor
public class GithubAuthTestController {

	@PostMapping("/login/github/get-token")
	public ResponseEntity<GithubAccessTokenResponse> githubGetToken(@RequestBody GithubLoginRequest githubLoginRequest) {
		String token = GithubTestResponses.findByCode(githubLoginRequest.getCode()).getAccessToken();
		return ResponseEntity.ok(new GithubAccessTokenResponse(token));
	}

	@GetMapping("/login/github/get-profile")
	public ResponseEntity<GithubProfileResponse> githubGetProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth) {
		String token = headerAuth.split(" ")[1];
		String email = GithubTestResponses.findByToken(token).getEmail();
		return ResponseEntity.ok().body(new GithubProfileResponse(email));
	}
}
