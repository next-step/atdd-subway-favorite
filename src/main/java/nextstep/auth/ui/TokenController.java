package nextstep.auth.ui;

import nextstep.favorite.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken_new(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("login/github")
    public ResponseEntity<TokenResponse> createTokenFromGithub(@RequestBody GithubAccessTokenRequest request) {
        TokenResponse response = tokenService.createGithubToken_new(request.getCode());

        return ResponseEntity.ok(response);
    }

}
