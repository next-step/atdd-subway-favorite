package nextstep.auth.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.GithubLoginRequest;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final AuthService authService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = authService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createTokenFromGithub(@RequestBody GithubLoginRequest request) {
        TokenResponse response = authService.createTokenFromGithub(request.getCode());

        return ResponseEntity.ok(response);
    }
}
