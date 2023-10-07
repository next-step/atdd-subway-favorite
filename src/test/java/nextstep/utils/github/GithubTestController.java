package nextstep.utils.github;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        String accessToken = GithubResponse.valueOfCode(request.getCode()).getAccessToken();
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "github", "profile", "bearer");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String accessTokenHeader) {
        String accessToken = accessTokenHeader.split(" ")[1];
        GithubResponse githubResponse = GithubResponse.valueOfAccessToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(githubResponse.getEmail(), githubResponse.getAge());
        return ResponseEntity.ok().body(response);
    }
}

