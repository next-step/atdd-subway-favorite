package nextstep.auth.ui;

import nextstep.auth.application.GithubService;
import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.GithubLoginRequest;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private TokenService tokenService;
    private GithubService githubService;

    public TokenController(TokenService tokenService, final GithubService githubService) {
        this.tokenService = tokenService;
        this.githubService = githubService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createTokenByGithub(@RequestBody GithubLoginRequest request) {
        TokenResponse response = githubService.createToken(request.getCode());

        return ResponseEntity.ok(response);
    }
}
