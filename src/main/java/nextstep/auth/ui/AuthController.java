package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.GithubLoginRequest;
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
    public TokenResponse login(@RequestBody final TokenRequest request) {
        return authService.login(request);
    }


    @PostMapping("/login/github")
    public TokenResponse login(@RequestBody final GithubLoginRequest request) {
        return authService.oauth2Login(request);
    }
}
