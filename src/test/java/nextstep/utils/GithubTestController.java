package nextstep.utils;

import nextstep.auth.AuthenticationException;
import nextstep.auth.GithubTestAccount;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        final String code = tokenRequest.getCode();

        final GithubTestAccount githubTestAccount = Arrays.stream(GithubTestAccount.values())
                .filter(githubAccount -> code.equals(githubAccount.getCode()))
                .findFirst()
                .orElseThrow(AuthenticationException::new);


        final String accessToken = githubTestAccount.getAccessToken();

        final GithubAccessTokenResponse tokenResponse = new GithubAccessTokenResponse(
                accessToken,
                "tokenType",
                "scope",
                "bearer");

        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        if (!"token".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new IllegalArgumentException("empty token");
        }

        final String accessToken = authorization.split(" ")[1];

        final GithubTestAccount githubTestAccount = Arrays.stream(GithubTestAccount.values())
                .filter(githubAccount -> accessToken.equals(githubAccount.getAccessToken()))
                .findFirst()
                .orElseThrow(AuthenticationException::new);


        final GithubProfileResponse testGithubProfile = new GithubProfileResponse(githubTestAccount.getEmail(), githubTestAccount.getAge());

        return ResponseEntity.ok().body(testGithubProfile);
    }
}

