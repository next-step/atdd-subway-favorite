package nextstep.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FakeGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = GithubResponse.getAccessTokenByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization;
        String email = GithubResponse.getEmailByAccessToken(accessToken);
        int age = GithubResponse.getAgeByAccessToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(email, age);
        return ResponseEntity.ok(response);
    }
}


