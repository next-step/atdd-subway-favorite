package nextstep.utils;

import nextstep.auth.token.acceptance.GithubResponses;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest request
    ) {
        GithubAccessTokenResponse githubAccessTokenResponse = GithubResponses
            .fromCode(request.getCode())
            .toGithubAccessTokenResponse();

        return ResponseEntity.ok().body(githubAccessTokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
        @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.split(" ")[1];

        GithubProfileResponse githubProfileResponse = GithubResponses
            .fromAccessToken(token)
            .toGithubProfileResponse();

        return ResponseEntity.ok().body(githubProfileResponse);
    }
}

