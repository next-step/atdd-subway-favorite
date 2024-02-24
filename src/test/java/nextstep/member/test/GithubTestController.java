package nextstep.member.test;

import nextstep.member.application.request.github.GithubAccessTokenRequest;
import nextstep.member.application.response.github.GithubAccessTokenResponse;
import nextstep.member.application.response.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    private static final String TOKEN_ISSUE_FAIL_MESSAGE = "ACCESS TOKEN ISSUE FAILED";

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String code = githubAccessTokenRequest.getCode();
        String accessToken = GithubResponses.getAccessTokenByCode(code);

        GithubAccessTokenResponse githubAccessTokenResponse = GithubAccessTokenResponse.from(accessToken);
        return ResponseEntity.ok(githubAccessTokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> userInfo(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses githubResponses = GithubResponses.getGithubResponseByAccessToken(accessToken);

        GithubProfileResponse githubProfileResponse = GithubProfileResponse.from(githubResponses.getEmail());
        return ResponseEntity.ok(githubProfileResponse);
    }

}
