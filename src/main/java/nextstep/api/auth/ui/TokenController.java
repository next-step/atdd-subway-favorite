package nextstep.api.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.api.auth.application.token.TokenRequest;
import nextstep.api.auth.application.token.TokenResponse;
import nextstep.api.auth.application.token.TokenService;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubTokenRequest;

@RestController
public class TokenController {
    private TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createTokenByGithub(@RequestBody GithubTokenRequest request) {
        TokenResponse response = tokenService.createTokenFromGithub(request.getCode());

        return ResponseEntity.ok(response);
    }
}
