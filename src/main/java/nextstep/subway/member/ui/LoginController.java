package nextstep.subway.member.ui;

import nextstep.subway.member.application.LoginService;
import nextstep.subway.member.application.dto.GithubTokenRequest;
import nextstep.subway.member.application.dto.TokenRequest;
import nextstep.subway.member.application.dto.TokenResponse;
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
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = loginService.createGithubToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createGithubToken(@RequestBody GithubTokenRequest request) {
        TokenResponse response = loginService.createGithubToken(request.getCode());

        return ResponseEntity.ok(response);
    }
}
