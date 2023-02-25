package nextstep.member.ui;

import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String accessToken = GithubResponses.findByCode(githubAccessTokenRequest.getCode()).getAccessToken();
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses githubResponse = GithubResponses.findByToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(githubResponse.getEmail());
        return ResponseEntity.ok(response);
    }
}