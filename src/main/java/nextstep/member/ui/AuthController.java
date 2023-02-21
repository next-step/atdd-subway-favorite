package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.web.bind.annotation.*;

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
}
