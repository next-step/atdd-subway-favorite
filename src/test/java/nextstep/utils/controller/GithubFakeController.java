package nextstep.utils.controller;

import nextstep.auth.fixture.GithubResponses;
import nextstep.auth.oauth.github.GithubAccessTokenRequest;
import nextstep.auth.oauth.github.GithubAccessTokenResponse;
import nextstep.auth.oauth.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubFakeController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses githubResponse = GithubResponses.lookUpByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubResponse.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
        @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses githubResponse = GithubResponses.lookUpByToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(githubResponse.getEmail());
        return ResponseEntity.ok(response);
    }
}
