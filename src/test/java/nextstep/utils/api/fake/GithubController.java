package nextstep.utils.api.fake;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.GithubTokenRequest;
import nextstep.auth.ui.dto.TokenResponseBody;
import nextstep.utils.fixture.GithubUserFixture;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
public class GithubController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<TokenResponseBody> accessToken(@RequestBody GithubTokenRequest tokenRequest) {
        GithubUserFixture user =  GithubUserFixture.findByCode(tokenRequest.getCode());
        TokenResponseBody response = new TokenResponseBody(user.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];

        GithubUserFixture githubUserFixture = GithubUserFixture.findByAccessToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(githubUserFixture.getEmail());
        return ResponseEntity.ok(response);
    }
}