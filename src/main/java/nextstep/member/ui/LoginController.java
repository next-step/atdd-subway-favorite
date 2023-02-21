package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.LoginService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login/token")
    public TokenResponse createToken(@RequestBody TokenRequest tokenRequest) {
        return loginService.createToken(tokenRequest);
    }
}
