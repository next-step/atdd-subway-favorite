package nextstep.auth.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.AuthService;

import nextstep.github.application.dto.GithubAccessTokenResponse;
import nextstep.github.application.dto.GithubRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.signIn(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/login/github")
    private ResponseEntity<GithubAccessTokenResponse> loginByGithub(@RequestBody GithubRequest githubRequest) {
        GithubAccessTokenResponse githubAccessTokenResponse = authService.signInByGithub(githubRequest);
        return ResponseEntity.ok().body(githubAccessTokenResponse);
    }
}
