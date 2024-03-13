package nextstep.test;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.ui.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses responses = GithubResponses.from(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(
            responses.getAccessToken(), "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
        @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses responses = GithubResponses.fromAccessToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(responses.getEmail(), 20);
        return ResponseEntity.ok(response);
    }
}

