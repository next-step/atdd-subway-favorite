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
        String accessToken = GithubResponses.getAccessTokenByCode(tokenRequest.getCode());

        GithubAccessTokenResponse response = GithubAccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String email = GithubResponses.getEmailByAccessToken(authorization);
        GithubProfileResponse response = GithubProfileResponse.builder()
                .email(email)
                .build();
        return ResponseEntity.ok(response);
    }
}
