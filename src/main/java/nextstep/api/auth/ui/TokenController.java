package nextstep.api.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.application.token.TokenRequest;
import nextstep.api.auth.application.token.TokenResponse;
import nextstep.api.auth.application.token.TokenService;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubTokenRequest;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody final TokenRequest request) {
        final var response = tokenService.createToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createTokenByGithub(@RequestBody final GithubTokenRequest request) {
        final var response = tokenService.createTokenFromGithub(request.getCode());
        return ResponseEntity.ok(response);
    }
}
