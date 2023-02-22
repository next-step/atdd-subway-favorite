package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.infrastructure.dto.GithubTokenRequest;
import nextstep.member.ui.request.TokenRequest;
import nextstep.member.ui.response.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> github(@RequestBody GithubTokenRequest request) {
        TokenResponse response = authService.getGithubToken(request);
        return ResponseEntity.ok(response);
    }
}
