package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest request) {

        return ResponseEntity.ok(GithubAccessTokenResponseFixture.of(request.getCode()));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(NativeWebRequest webRequest) {

        String authorization = webRequest.getHeader("Authorization");
        String token = authorization.split(" ")[1];

        return ResponseEntity.ok(
            GithubProfileResponseFixture.of(GithubResponse.findByToken(token).getEmail())
        );
    }
}

