package nextstep.member.ui;

import nextstep.member.application.LoginService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> getToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok().body(loginService.authorize(request));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> getToken(@RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok().body(loginService.authorize(request));
    }
}
