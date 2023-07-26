package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String clientId = githubAccessTokenRequest.getClient_id();
        String clientSecret = githubAccessTokenRequest.getClient_secret();
        String code = githubAccessTokenRequest.getCode();

        if (StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret) || StringUtils.isBlank(code)) {
            return ResponseEntity.badRequest().build();
        }

        GithubAccessTokenResponse response = new GithubAccessTokenResponse("githubAccessToken: " + code, "testType", "testScope", "test");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader HttpHeaders headers) {
        List<String> authorization = headers.get("Authorization");
        String githubAccessToken = authorization.get(0);

        if (StringUtils.isBlank(githubAccessToken)) {
            return ResponseEntity.badRequest().build();
        }

        GithubProfileResponse response = new GithubProfileResponse("email", 12);
        return ResponseEntity.ok().body(response);
    }
}

