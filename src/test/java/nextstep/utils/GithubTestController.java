package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GithubTestController {

    private static final String PARAMETER_CODE = "code";

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody Map<String, String> body) {
        String code = body.get(PARAMETER_CODE);
        GithubResponse githubResponse = GithubResponse.fromCode(code);
        return ResponseEntity.ok().body(new GithubAccessTokenResponse(githubResponse.getAccessToken(), "tokenType", "scope", "bearer"));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponse githubResponse = GithubResponse.fromToken(accessToken);

        if (githubResponse == GithubResponse.잘못된_토큰) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }
        GithubProfileResponse response = new GithubProfileResponse(githubResponse.getEmail(), githubResponse.getAge());
        return ResponseEntity.ok().body(response);
    }
}

