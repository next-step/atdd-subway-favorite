package nextstep.auth.ui;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.acceptance.GithubResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses githubUser = GithubResponses.findByCode(tokenRequest.getCode());

        GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubUser.getAccessToken(), "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") final String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses githubUser = GithubResponses.findByAccessToken(accessToken);

        GithubProfileResponse response = new GithubProfileResponse(githubUser.getEmail(), githubUser.getAge());
        return ResponseEntity.ok(response);
    }
}
