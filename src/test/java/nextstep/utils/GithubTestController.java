package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.utils.mock.GithubResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubResponses githubResponses = GithubResponses.findByCode(request.getCode());

        return ResponseEntity.ok().body(githubResponses.toGithubAccessTokenResponse());
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String accessToken) {
        GithubResponses githubResponses = GithubResponses.findByAccessToken(accessToken.split(" ")[1]);
        return ResponseEntity.ok(githubResponses.toGithubProfileResponse());
    }
}

