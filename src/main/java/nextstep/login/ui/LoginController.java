package nextstep.login.ui;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import nextstep.login.application.LoginService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/login")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse generateToken(@RequestBody TokenRequest tokenRequest) {
        Preconditions.checkArgument(tokenRequest.getEmail() != null);
        Preconditions.checkArgument(tokenRequest.getPassword() != null);

        return loginService.generateToken(tokenRequest);
    }
}
