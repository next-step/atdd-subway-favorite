package nextstep.member;

import nextstep.member.application.dto.OAuthAccessTokenRequest;
import nextstep.member.application.dto.OAuthProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<TokenResponse> accessToken(
            @RequestBody OAuthAccessTokenRequest tokenRequest) {
        GithubResponses user = GithubResponses.getUserByCode(tokenRequest.getCode());
        return ResponseEntity.ok(new TokenResponse(user.getAccessToken()));
    }

    @GetMapping("/github/user")
    public ResponseEntity<OAuthProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        GithubResponses user = GithubResponses.getUserByToken(authorization);
        return ResponseEntity.ok(new OAuthProfileResponse(user.getEmail(), user.getAge()));
    }
}
