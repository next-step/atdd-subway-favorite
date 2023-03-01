package nextstep.auth.ui;

import antlr.Token;
import lombok.RequiredArgsConstructor;
import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.GithubLoginRequest;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest tokenRequest) {
        final TokenResponse tokenResponse = authService.login(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> githubLogin(@RequestBody final GithubLoginRequest githubLoginRequest) {
        final TokenResponse tokenResponse = authService.githubLogin(githubLoginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
