package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.auth.dto.TokenRequest;
import nextstep.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoController {
    private AuthService authService;

    public AutoController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.login(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
