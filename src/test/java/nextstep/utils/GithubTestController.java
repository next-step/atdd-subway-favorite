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

    public static final String TOKEN_TYPE = "access_token";
    public static final String SCOPE = "profile";
    public static final String BEARER = "bearer";

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
        if (!"token".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];
        Optional<GithubResponses> githubResponses = GithubResponses.getGithubResponses(token);
        if (githubResponses.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GithubProfileResponse githubProfileResponse = githubResponses.get().toGithubProfileResponse();
        return ResponseEntity.ok().body(githubProfileResponse);
    }
}

