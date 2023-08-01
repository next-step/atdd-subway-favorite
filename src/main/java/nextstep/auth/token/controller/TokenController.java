package nextstep.auth.token.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.token.dto.TokenRequest;
import nextstep.auth.token.dto.TokenResponse;
import nextstep.auth.token.service.TokenService;
import nextstep.auth.oauth2.github.dto.GithubTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

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
