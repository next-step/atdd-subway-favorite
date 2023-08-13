package nextstep.github;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.auth.token.oauth2.github.GithubTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FakeGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubTokenRequest request) {
        FakeGithubResponse fakeGithubResponse = FakeGithubResponse.findByCode(request.getCode());
        GithubAccessTokenResponse response = toTokenResponse(fakeGithubResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        FakeGithubResponse fakeGithubResponse = FakeGithubResponse.findByAccessToken(authorization);
        FakeGithubProfileResponse fakeGithubProfileResponse = FakeGithubProfileResponse
                .findByEmail(fakeGithubResponse.getEmail());
        GithubProfileResponse response = new GithubProfileResponse(fakeGithubProfileResponse.getEmail(),
                fakeGithubProfileResponse.getAge());

        return ResponseEntity.ok(response);
    }

    private GithubAccessTokenResponse toTokenResponse(FakeGithubResponse fakeGithubResponse) {
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(fakeGithubResponse.getAccessToken(),
                "token_type_test", "scope_test", "scope_test");
        return response;
    }
}