package nextstep.support;

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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user() {
        return ResponseEntity.ok().build();
    }
}

