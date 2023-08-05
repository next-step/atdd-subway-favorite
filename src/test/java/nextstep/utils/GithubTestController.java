package nextstep.utils;

import java.util.Optional;
import nextstep.auth.AuthenticationException;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String code = githubAccessTokenRequest.getCode();
        Optional<String> accessToken = GithubResponses.getAccessToken(code);
        if (accessToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GithubAccessTokenResponse githubAccessTokenResponse = GithubAccessTokenResponse.builder()
                .accessToken(accessToken.get())
                .tokenType(TOKEN_TYPE)
                .scope(SCOPE)
                .bearer(BEARER)
                .build();
        return ResponseEntity.ok().body(githubAccessTokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (!TOKEN_KEY.equalsIgnoreCase(authorization.split(DELIMITER)[TOKEN_KEY_INDEX])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(DELIMITER)[TOKEN_INDEX];
        Optional<GithubResponses> githubResponses = GithubResponses.getGithubResponses(token);
        if (githubResponses.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GithubProfileResponse githubProfileResponse = githubResponses.get().toGithubProfileResponse();
        return ResponseEntity.ok().body(githubProfileResponse);
    }
}

