package nextstep.core.member.presentation;

import nextstep.core.member.application.dto.TokenRequest;
import nextstep.core.member.application.dto.TokenResponse;
import nextstep.core.member.application.TokenService;
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
        return ResponseEntity.ok(tokenService.createToken(request.getEmail(), request.getPassword()));
    }
}
