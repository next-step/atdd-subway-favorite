package nextstep.utils.api.fake;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.utils.fixture.GithubUserFixture;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
public class GithubController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<TokenResponse> accessToken(@RequestBody GithubTokenRequest tokenRequest) {
        GithubUserFixture user =  GithubUserFixture.findByCode(tokenRequest.getCode());
        TokenResponse response = new TokenResponse(user.getAccessToken());
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