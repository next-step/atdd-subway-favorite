package nextstep.member.ui;

import java.time.LocalDateTime;
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

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> loginBy(@RequestBody final TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.createTokenFrom(tokenRequest, LocalDateTime.now()));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> loginBy(@RequestBody final GithubLoginRequest githubLoginRequest) {
        return ResponseEntity.ok(authService.createTokenFrom(githubLoginRequest));
    }
}
