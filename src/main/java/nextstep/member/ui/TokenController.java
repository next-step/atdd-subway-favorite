package nextstep.member.ui;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenDto;
import nextstep.member.ui.dto.TokenRequestBody;
import nextstep.member.ui.dto.TokenResponseBody;
import nextstep.member.ui.dto.TokenFromGithubRequestBody;
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
    public ResponseEntity<TokenResponseBody> createToken(@RequestBody TokenRequestBody request) {
        TokenDto token = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(TokenResponseBody.create(token.getAccessToken()));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponseBody> createTokenFromGithub(@RequestBody TokenFromGithubRequestBody request) {
        TokenDto token = tokenService.createTokenFromGithub(request.getCode());

        return ResponseEntity.ok(TokenResponseBody.create(token.getAccessToken()));
    }
}
