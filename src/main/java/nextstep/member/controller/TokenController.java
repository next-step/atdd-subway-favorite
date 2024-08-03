package nextstep.member.controller;

import nextstep.member.controller.dto.OAuthCodeRequest;
import nextstep.member.domain.command.TokenCommander;
import nextstep.member.controller.dto.TokenRequest;
import nextstep.member.controller.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private final TokenCommander tokenCommander;

    public TokenController(TokenCommander tokenCommander) {
        this.tokenCommander = tokenCommander;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = new TokenResponse(tokenCommander.createToken(request.getEmail(), request.getPassword()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createToken(@RequestBody OAuthCodeRequest request) {
        TokenResponse response = new TokenResponse("토큰토큰");
        return ResponseEntity.ok(response);
    }
}
