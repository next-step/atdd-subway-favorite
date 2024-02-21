package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
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
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> githubLogin(@RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok(authService.loginGithub(request.getCode()));
    }
}
