package nextstep.member.ui;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!test")
public class GithubController {

    private final TokenService tokenService;

    public GithubController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> login(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        TokenResponse tokenResponse = tokenService.login(githubAccessTokenRequest.getCode());
        return ResponseEntity.ok(tokenResponse);
    }
}
