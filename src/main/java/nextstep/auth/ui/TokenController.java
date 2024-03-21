package nextstep.auth.ui;

import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.TokenDto;
import nextstep.auth.ui.dto.TokenRequestBody;
import nextstep.auth.ui.dto.TokenResponseBody;
import nextstep.auth.ui.dto.TokenFromGithubRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseBody> createToken(@RequestBody TokenRequestBody request) {
        TokenDto token = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(TokenResponseBody.create(token.getAccessToken()));
    }

    @PostMapping("/auth/login/github")
    public ResponseEntity<TokenResponseBody> createTokenFromGithub(@RequestBody TokenFromGithubRequestBody request) {
        TokenDto token = tokenService.createTokenFromGithub(request.getCode());

        return ResponseEntity.ok(TokenResponseBody.create(token.getAccessToken()));
    }
}
