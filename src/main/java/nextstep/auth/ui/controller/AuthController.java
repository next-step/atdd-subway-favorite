package nextstep.auth.ui.controller;

import nextstep.auth.application.dto.AuthRequest;
import nextstep.auth.application.dto.AuthResponse;
import nextstep.auth.application.dto.GithubAuthRequest;
import nextstep.auth.application.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/token")
    public ResponseEntity<AuthResponse> createToken(@RequestBody AuthRequest request) {
        AuthResponse response = authService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/token/github")
    public ResponseEntity<AuthResponse> createGithubToken(@RequestBody GithubAuthRequest request) {
        AuthResponse response = authService.createGithubToken(request.getCode());

        return ResponseEntity.ok(response);
    }
}
