package nextstep.member;

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
        GithubResponses user = GithubResponses.getUserByCode(tokenRequest.getCode());
        return ResponseEntity.ok(new GithubAccessTokenResponse(user.getAccessToken()));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        GithubResponses user = GithubResponses.getUserByToken(authorization);
        return ResponseEntity.ok(new GithubProfileResponse(user.getEmail(), user.getAge()));
    }
}
