package nextstep.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = GithubResponse.ofCode(tokenRequest.getCode()).getAccessToken();
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "", "");
        return ResponseEntity.ok(response);
    }
}