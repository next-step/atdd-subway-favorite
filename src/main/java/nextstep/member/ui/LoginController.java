package nextstep.member.ui;

import nextstep.member.application.LoginService;
import nextstep.member.application.dto.CodeRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("login")
@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest request) {
        TokenResponse tokenResponse = loginService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("github")
    public ResponseEntity<TokenResponse> loginGithub(@RequestBody CodeRequest request) {
        TokenResponse tokenResponse = loginService.githubLogin(request);
        return ResponseEntity.ok(tokenResponse);
    }
}
