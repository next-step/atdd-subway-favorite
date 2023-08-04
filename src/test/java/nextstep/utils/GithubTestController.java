package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok().body(
            new GithubAccessTokenResponse(
                "accessToken",
                "bearer",
                "repo,gist",
                "USER_ACCESS_TOKEN"
            ));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user() {
        return ResponseEntity.ok().body(
            new GithubProfileResponse(
                "kskyung0624@gmail.com",
                20
            )
        );
    }
}

