package nextstep.utils;

import static nextstep.utils.GithubMockResponses.*;
import static org.springframework.http.ResponseEntity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import nextstep.client.github.dto.GithubAccessTokenRequest;
import nextstep.client.github.dto.GithubAccessTokenResponse;
import nextstep.client.github.dto.GithubUserProfileResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/17
 */
@RestController
public class GithubTestController {

	@PostMapping("/github/login/oauth/access-token")
	public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
		return ok(GithubAccessTokenResponse.of(fetchAccessTokenByCode(tokenRequest.getCode()), "repo,gist", "tokenType"));
	}

	@GetMapping("/github/user")
	public ResponseEntity<GithubUserProfileResponse> user(@RequestHeader("Authorization") String authorization) {
		String accessToken = authorization.split(" ")[1];
		return ok(GithubUserProfileResponse.of(fetchEmailByAccessToken(accessToken), fetchIdByAccessToken(accessToken), fetchLoginByAccessToken(accessToken)));
	}
}
