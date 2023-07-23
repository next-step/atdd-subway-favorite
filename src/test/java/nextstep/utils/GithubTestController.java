package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken() {
        return ResponseEntity.ok().body(new GithubAccessTokenResponse("access_token", "tokenType", "scope", "bearer"));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user() {
        return ResponseEntity.ok().body(new GithubProfileResponse("test@aabc.com", 48));
    }
}

