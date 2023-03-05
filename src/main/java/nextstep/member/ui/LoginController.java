package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = authService.authByBasic(tokenRequest.getEmail(), tokenRequest.getPassword());

        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> loginByGithub(@RequestBody GithubTokenRequest githubTokenRequest) {
        TokenResponse token = authService.authByGithub(githubTokenRequest.getCode());

        return ResponseEntity.ok().body(token);
    }
}
