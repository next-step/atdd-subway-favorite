package nextstep.member.acceptance;

import auth.GithubAccessTokenRequest;
import auth.GithubAccessTokenResponse;
import auth.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest
    ) {
        GithubAccessTokenResponse response = new GithubAccessTokenResponse("access token", "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization
    ) {
        String accessToken = authorization.split(" ")[1];
        GithubProfileResponse response = new GithubProfileResponse("admin@email.com", 20);
        return ResponseEntity.ok(response);
    }
}
