package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "login")
public class AuthController {

    private AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("token")
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest request) {
        final TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("github")
    public ResponseEntity<GithubAccessTokenResponse> loginByGithub(@RequestBody final GithubTokenRequest request) {
        final GithubAccessTokenResponse response = authService.loginByGithub(request);
        return ResponseEntity.ok(response);
    }
}
