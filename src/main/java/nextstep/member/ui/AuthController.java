package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        return ResponseEntity.ok().body(authService.login(request));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> loginWithGithub(@RequestBody GithubLoginRequest request) {
        return ResponseEntity.ok().body(authService.loginWithGithub(request));
    }

}
