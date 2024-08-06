package nextstep.member;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {

        GithubResponses user = GithubResponses.getUserByCode(tokenRequest.getCode());

        String accessToken = user.getAccessToken();
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> getProfile(
        @RequestHeader("Authorization") String accessToken) {

        GithubResponses user = GithubResponses.getUserByAccessToken(accessToken);

        GithubProfileResponse response = new GithubProfileResponse(user.getEmail());

        return ResponseEntity.ok(response);
    }
}
