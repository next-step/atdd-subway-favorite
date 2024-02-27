package nextstep.utils;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> getGithubToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String accessToken = GithubResponses.findAccessToken(githubAccessTokenRequest.getCode()).orElse("access_token");
        return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken, "", "", ""));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        String githubToken = authorization.split(" ")[1];
        String email = GithubResponses.findEmail(githubToken).orElse("email@email.com");
        GithubProfileResponse response = new GithubProfileResponse(email, 20);
        return ResponseEntity.ok(response);
    }
}
