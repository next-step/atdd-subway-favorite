package nextstep.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import nextstep.auth.acceptance.GithubUserFixture;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubUserFixture findUser = GithubUserFixture.findByCode(request.getCode());
        return ResponseEntity.ok()
            .body(findUser.toGithubAccessTokenResponse());
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String accessToken) {
        GithubUserFixture findUser = GithubUserFixture.findByAccessToken(accessToken.split(" ")[1]);
        return ResponseEntity.ok()
            .body(findUser.toGithubProfileResponse());
    }
}

