package nextstep.member.ui;

import nextstep.member.application.GithubOAuth2Service;
import nextstep.security.payload.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    private final GithubOAuth2Service githubOAuth2Service;

    public OAuth2Controller(final GithubOAuth2Service githubOAuth2Service) {
        this.githubOAuth2Service = githubOAuth2Service;
    }

    @GetMapping("/login/code/github")
    public ResponseEntity<TokenResponse> createToken(String code) {
        return ResponseEntity.ok(githubOAuth2Service.createToken(code));
    }
}
