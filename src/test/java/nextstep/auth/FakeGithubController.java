package nextstep.auth;

import nextstep.auth.infra.dto.GithubAccessTokenRequest;
import nextstep.auth.infra.dto.GithubAccessTokenResponse;
import nextstep.auth.infra.dto.GithubProfileResponse;
import nextstep.auth.fixture.GithubResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FakeGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses githubResponses = GithubResponses.of(tokenRequest.getCode());
        if (githubResponses.isUnAuthorized()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubResponses.getToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        String email = GithubResponses.getEmailOfToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(email, 20);
        return ResponseEntity.ok(response);
    }
}
