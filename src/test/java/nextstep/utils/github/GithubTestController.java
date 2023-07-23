package nextstep.utils.github;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    private final GithubTestService githubTestService;

    public GithubTestController(GithubTestService githubTestService) {
        this.githubTestService = githubTestService;
    }

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok().body(githubTestService.accessToken(request));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(githubTestService.getProfile(authorization));
    }
}
