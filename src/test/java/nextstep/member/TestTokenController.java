package nextstep.member;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestTokenController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponse user = GithubResponse.findByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(user.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        GithubResponse user = GithubResponse.findByAccessToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(user.getEmail(), 20);
        return ResponseEntity.ok(response);
    }
}
