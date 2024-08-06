package nextstep.member;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {

        GithubResponses 사용자 = GithubResponses.getUserByCode(tokenRequest.getCode());

        String accessToken = 사용자.getAccessToken();
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }
}
