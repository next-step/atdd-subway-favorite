package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.study.AuthAcceptanceTest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nextstep.study.AuthAcceptanceTest.AGE;

@RestController
public class GithubTestController {
    public static final String CODE = "code";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String TOKEN_TYPE = "token_type";
    public static final String SCOPE = "scope";
    public static final String BEARER = "bearer";

    private static final String TOKEN = "token";


    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        if (CODE.equals(request.getCode())) {
            return ResponseEntity.ok(new GithubAccessTokenResponse(ACCESS_TOKEN, TOKEN_TYPE, SCOPE, BEARER));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValid(authorizationHeader)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new GithubProfileResponse(AuthAcceptanceTest.EMAIL, AGE));
    }

    private boolean isValid(String authorizationHeader) {
        String[] split = authorizationHeader.split(StringUtils.SPACE);
        if (split.length != 2) {
            return false;
        }
        return TOKEN.equalsIgnoreCase(split[0]);
    }
}

