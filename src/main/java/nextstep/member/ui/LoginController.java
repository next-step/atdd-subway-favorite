package nextstep.member.ui;

import nextstep.member.application.LoginService;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = loginService.loginToken(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> loginGithub(@RequestBody GithubTokenRequest githubTokenRequest) {
        TokenResponse tokenResponse = loginService.loginGithubToken(githubTokenRequest.getCode());
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * Github OAuth Callback API
     * @param code
     * @return
     */
    @GetMapping("/oauth/authorize")
    public ResponseEntity<TokenResponse> loginGithub(@RequestParam("code") String code) {
        TokenResponse tokenResponse = loginService.loginGithubToken(code);
        return ResponseEntity.ok(tokenResponse);
    }
}
