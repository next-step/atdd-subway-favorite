package nextstep.member.ui;

import nextstep.member.application.LoginService;
import nextstep.member.application.dto.LoginResponse;
import nextstep.member.application.dto.TokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<LoginResponse> login(@RequestBody TokenRequest request) {
        LoginResponse response = loginService.tokenLogin(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<LoginResponse> githubLogin(@RequestBody GithubLoginRequest request) {
        LoginResponse response = loginService.githubLogin(request.getCode());
        return ResponseEntity.ok(response);
    }
}
