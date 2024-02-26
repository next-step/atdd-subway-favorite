package nextstep.member.ui;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.*;
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
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<GithubTokenResponse> getAccessToken(@RequestBody GithubTokenRequest request) {
        GithubTokenResponse response = tokenService.getAccessToken(request);

        return ResponseEntity.ok(response);
    }
}
