package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        GithubTestUser githubTestUser = GithubTestUser.findUserByCode(githubAccessTokenRequest.getCode());
        if (githubTestUser == null) {
            return ResponseEntity.ok().body(new GithubAccessTokenResponse());
        }

        GithubAccessTokenResponse githubAccessTokenResponse = new GithubAccessTokenResponse(githubTestUser.getAccessToken(),
                "tokenType",
                "scope",
                "bearer");
        return ResponseEntity.ok().body(githubAccessTokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader(value = "Authorization") String authorization) {
        GithubTestUser githubTestUser = GithubTestUser.findUserByAccessToken(authorization);
        if (githubTestUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
        }

        GithubProfileResponse githubProfileResponse = new GithubProfileResponse(githubTestUser.getEmail(), githubTestUser.getAge());
        return ResponseEntity.ok().body(githubProfileResponse);
    }
}

