package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        TokenResponse token = authService.createToken(request);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> githubLogin(@RequestBody GithubTokenRequest request) {
        TokenResponse token = authService.getGithubToken(request);
        return ResponseEntity.ok().body(token);
    }
}
