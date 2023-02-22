package nextstep.subway.utils;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeLoginController {

    private final AuthService fakeAuthService;

    public FakeLoginController(final AuthService fakeAuthService) {
        this.fakeAuthService = fakeAuthService;
    }

    @PostMapping("/fake/login/github")
    public ResponseEntity<TokenResponse> loginGithub(@RequestBody GithubTokenRequest githubTokenRequest) {
        final var token = fakeAuthService.createToken(githubTokenRequest);
        return ResponseEntity.ok().body(token);
    }
}
