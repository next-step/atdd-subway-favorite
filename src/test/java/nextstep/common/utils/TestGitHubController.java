package nextstep.common.utils;

import nextstep.core.auth.application.dto.GithubProfileResponse;
import nextstep.core.auth.application.dto.GithubAccessTokenRequest;
import nextstep.core.auth.application.dto.GithubAccessTokenResponse;
import nextstep.core.auth.fixture.GithubMemberFixture;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
public class TestGitHubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> createToken(@RequestBody GithubAccessTokenRequest request) {
        GithubAccessTokenResponse githubAccessTokenResponse = new GithubAccessTokenResponse(GithubMemberFixture.findTokenByCode(
                request.getCode()), "repo,gist", "bearer");
        return ResponseEntity.ok().body(githubAccessTokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> findAuthenticateUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(new GithubProfileResponse(GithubMemberFixture.findMemberByToken(token)));
    }
}