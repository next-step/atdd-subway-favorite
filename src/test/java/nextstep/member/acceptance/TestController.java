package nextstep.member.acceptance;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponse githubResponse = GithubResponse.findByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubResponse.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
        @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];

        GithubResponse githubResponse = GithubResponse.findByAccessToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(githubResponse.getEmail(), githubResponse.getAge());
        return ResponseEntity.ok(response);
    }
}