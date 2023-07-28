package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class GithubTestController {
    private static final String TOKEN_TYPE = "token_type";
    private static final String SCOPE = "scope";
    private static final String BEARER = "bearer";
    private static final String TOKEN = "TOKEN";

    private GithubResponse githubResponse = GithubResponse.사용자;

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        if (githubResponse.getCode().equals(request.getCode())) {
            return ResponseEntity.ok(new GithubAccessTokenResponse(
                    githubResponse.getAccessToken(), TOKEN_TYPE, SCOPE, BEARER));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValid(authorizationHeader)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new GithubProfileResponse(githubResponse.getEmail(), githubResponse.getAge()));
    }

    private boolean isValid(String authorizationHeader) {
        String[] split = authorizationHeader.split(StringUtils.SPACE);
        if (split.length != 2) {
            return false;
        }
        return TOKEN.equalsIgnoreCase(split[0]);
    }
}

