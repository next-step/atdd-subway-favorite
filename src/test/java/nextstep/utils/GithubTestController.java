package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static nextstep.utils.GithubTestController.GithubResponse.*;

@RestController
public class GithubTestController {

    public enum GithubResponse {

        테스트_유저1("accessToken_1", "user1@github.com", 20);
        public final String accessToken;
        public final String email;
        public final Integer age;

        GithubResponse(String accessToken, String email, Integer age) {
            this.accessToken = accessToken;
            this.email = email;
            this.age = age;
        }
    }

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken() {
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(테스트_유저1.accessToken, "bearer", "repo,gist", "");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user() {
        GithubProfileResponse response = new GithubProfileResponse(테스트_유저1.email, 테스트_유저1.age);
        return ResponseEntity.ok().body(response);
    }
}

