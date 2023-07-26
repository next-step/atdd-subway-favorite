package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        GithubResponse githubResponse = GithubResponse.fromCode(code);
        return ResponseEntity.ok().body(new GithubAccessTokenResponse(githubResponse.getAccessToken(), "tokenType", "scope", "bearer"));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponse githubResponse = GithubResponse.fromToken(accessToken);
        return ResponseEntity.ok().body(new GithubProfileResponse(githubResponse.getEmail(), githubResponse.getAge()));
    }
}

