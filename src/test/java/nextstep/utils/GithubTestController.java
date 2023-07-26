package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {
    private final GithubTestRepository githubTestRepository;

    public GithubTestController(GithubTestRepository githubTestRepository) {
        this.githubTestRepository = githubTestRepository;
    }

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String clientId = githubAccessTokenRequest.getClient_id();
        String clientSecret = githubAccessTokenRequest.getClient_secret();
        String code = githubAccessTokenRequest.getCode();

        if (StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret) || StringUtils.isBlank(code)) {
            return ResponseEntity.badRequest().build();
        }

        GithubAccessTokenResponse response = new GithubAccessTokenResponse("githubAccessToken: " + code, "bearer", "testScope", "test");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String githubAccessToken) {
        if (StringUtils.isBlank(githubAccessToken)) {
            return ResponseEntity.badRequest().build();
        }

        GithubProfileResponse response = new GithubProfileResponse("email", 12);
        return ResponseEntity.ok().body(response);
    }
}

