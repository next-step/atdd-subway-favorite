package nextstep.utils;

import java.util.Arrays;
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

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        Optional<String> accessToken = Arrays.stream(GithubResponses.values())
                .filter(githubResponse -> githubResponse.getCode().equals(githubAccessTokenRequest.getCode()))
                .map(GithubResponses::getAccessToken)
                .findFirst();
        if (accessToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GithubAccessTokenResponse githubAccessTokenResponse =
                new GithubAccessTokenResponse(accessToken.get(), "access_token", "profile", "bearer");
        return ResponseEntity.ok().body(githubAccessTokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (!"token".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];
        Optional<GithubResponses> githubResponses = Arrays.stream(GithubResponses.values())
                .filter(githubResponse -> githubResponse.getAccessToken().equals(token))
                .findFirst();
        if (githubResponses.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GithubProfileResponse githubProfileResponse = new GithubProfileResponse(githubResponses.get().getEmail(),
                githubResponses.get().getAge());
        return ResponseEntity.ok().body(githubProfileResponse);
    }
}

