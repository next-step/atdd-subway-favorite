package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.AuthRequest;
import nextstep.auth.application.dto.AuthResponse;
import nextstep.auth.application.dto.OAuth2Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<AuthResponse> login(@RequestBody final AuthRequest request) {
        final AuthResponse response = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<AuthResponse> loginGithub(@RequestBody final OAuth2Request request) {
        final AuthResponse response = authService.loginGithub(request.getCode());

        return ResponseEntity.ok(response);
    }
}
