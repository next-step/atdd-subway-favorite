package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.subway.applicaion.dto.CodeRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public TokenResponse loginMember(@RequestBody TokenRequest tokenRequest) {
        return authService.loginMember(tokenRequest);
    }

    @PostMapping("/github")
    public TokenResponse loginGithub(@RequestBody CodeRequest codeRequest) {
        return authService.loginGithub(codeRequest.getCode());
    }
}
