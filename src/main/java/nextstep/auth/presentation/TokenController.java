package nextstep.auth.presentation;

import nextstep.auth.application.TokenService;
import nextstep.auth.presentation.dto.TokenRequest;
import nextstep.auth.presentation.dto.TokenResponse;
import nextstep.auth.infra.dto.OAuthRequest;
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


    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createToke(@RequestBody OAuthRequest request) {
        TokenResponse response = tokenService.createToken(request.getCode());
        return ResponseEntity.ok(response);
    }
}
