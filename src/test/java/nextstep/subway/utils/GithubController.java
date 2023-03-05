package nextstep.subway.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {

	@PostMapping("/github/access-token")
	public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
		String accessToken = GithubResponses.fromCode(request.getCode()).getAccessToken();
		return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken));
	}

	@GetMapping("/github/profile")
	public ResponseEntity<GithubProfileResponse> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		String accessToken = authorization.split(" ")[1];
		String email = GithubResponses.fromAccessToken(accessToken).getEmail();
		long id = GithubResponses.fromAccessToken(accessToken).getId();
		return ResponseEntity.ok(new GithubProfileResponse(id, email));
	}
}
