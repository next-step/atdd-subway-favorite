package nextstep.auth.presentation;

import nextstep.auth.GithubResponses;
import nextstep.auth.dto.GithubAccessTokenRequest;
import nextstep.auth.dto.GithubAccessTokenResponse;
import nextstep.auth.dto.GithubProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FakeGithubController {


    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses githubResponses = GithubResponses.getCode(tokenRequest.getClientId());
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
