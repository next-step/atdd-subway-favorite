package nextstep.auth.fake;

import nextstep.auth.infrastructure.GithubAccessTokenRequest;
import nextstep.auth.infrastructure.GithubAccessTokenResponse;
import nextstep.auth.infrastructure.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FakeGithubController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        var githubResponse = GithubUsers.findByCode(tokenRequest.getCode());
        return ResponseEntity.ok(new GithubAccessTokenResponse(githubResponse.getAccessToken()));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        var accessToken = authorization.split(" ")[1];
        var githubUser = GithubUsers.findByAccessToken(accessToken);
        var response = new GithubProfileResponse(githubUser.getEmail());
        return ResponseEntity.ok(response);
    }
}
