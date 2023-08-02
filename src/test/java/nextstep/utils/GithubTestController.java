package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.utils.mock.GithubResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    private static final String TOKEN_TYPE = "access_token";
    private static final String SCOPE = "profile";
    private static final String BEARER = "bearer";
    private static final int TOKEN_INDEX = 1;
    private static final String DELIMITER = " ";
    private static final int TOKEN_KEY_INDEX = 0;
    private static final String TOKEN_KEY = "token";

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubResponses githubResponses = GithubResponses.findByCode(request.getCode());

        return ResponseEntity.ok().body(githubResponses.toGithubAccessTokenResponse());
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String accessToken) {
        GithubResponses githubResponses = GithubResponses.findByAccessToken(accessToken.split(" ")[1]);
        return ResponseEntity.ok(githubResponses.toGithubProfileResponse());
    }
}

