package nextstep.auth.acceptance;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.OAuth2ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = GithubResponses.getAccessToken(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<OAuth2ProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses res = GithubResponses.getResponse(accessToken);
        OAuth2ProfileResponse response = new OAuth2ProfileResponse(res.getEmail(), res.getAge());
        return ResponseEntity.ok(response);
    }
}
