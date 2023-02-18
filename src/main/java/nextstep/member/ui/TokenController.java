package nextstep.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;

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

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createGitHubToken() {
        return ResponseEntity.ok(new TokenResponse("fasdfasd.fasdfasd.fasdfasd"));
    }
}
