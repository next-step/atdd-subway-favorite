package nextstep.member.controller;

import nextstep.member.domain.command.TokenService;
import nextstep.member.controller.dto.TokenRequest;
import nextstep.member.controller.dto.TokenResponse;
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
        TokenResponse response = new TokenResponse(tokenService.createToken(request.getEmail(), request.getPassword()));
        return ResponseEntity.ok(response);
    }
}
