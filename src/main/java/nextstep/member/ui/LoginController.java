package nextstep.member.ui;

import nextstep.member.application.LoginService;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.GithubLoginResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
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
    public TokenResponse login(@RequestBody TokenRequest tokenRequest) {
        return new TokenResponse(loginService.login(tokenRequest));
    }

    @PostMapping("/login/github")
    public GithubLoginResponse githubLogin(@RequestBody GithubLoginRequest tokenRequest) {
        return GithubLoginResponse.from(loginService.githubLogin(tokenRequest));
    }

}
