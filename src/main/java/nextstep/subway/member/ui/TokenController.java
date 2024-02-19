package nextstep.subway.member.ui;

import nextstep.subway.member.application.TokenService;
import nextstep.subway.member.application.dto.GithubTokenRequest;
import nextstep.subway.member.application.dto.TokenRequest;
import nextstep.subway.member.application.dto.TokenResponse;
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
    public ResponseEntity<TokenResponse> createGithubToken(@RequestBody GithubTokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getCode());

        return ResponseEntity.ok(response);
    }
}
