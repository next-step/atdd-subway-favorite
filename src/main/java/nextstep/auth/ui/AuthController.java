package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.auth.dto.GithubLoginRequest;
import nextstep.auth.dto.TokenRequest;
import nextstep.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.login(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> loginGithub(@RequestBody GithubLoginRequest githubLoginRequest) {
        TokenResponse tokenResponse = authService.loginGithub(githubLoginRequest.getCode());
        return ResponseEntity.ok().body(tokenResponse);
    }
}
