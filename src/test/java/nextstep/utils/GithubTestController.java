package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubAccessTokenResponse accessToken = GithubTestResponses.createAccessToken(request.getCode());
        return ResponseEntity.ok().body(accessToken);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader(value = "Authorization") String header) {
        String accessToken = AuthorizationHeader.findAccessToken(header);
        GithubProfileResponse githubProfile = GithubTestResponses.createGithubProfile(accessToken);
        return ResponseEntity.ok().body(githubProfile);
    }
}

