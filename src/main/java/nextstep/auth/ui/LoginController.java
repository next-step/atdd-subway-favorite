package nextstep.auth.ui;


import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> createLoginToken (@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok().body(authService.creteToken(tokenRequest));
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> createLoginGithubToken (@RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok().body(authService.createTokenByGitHub(request));
    }
}
