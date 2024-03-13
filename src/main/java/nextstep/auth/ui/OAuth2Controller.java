package nextstep.auth.ui;

import nextstep.auth.application.GithubLoginService;
import nextstep.auth.application.dto.OAuth2Request;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    private final GithubLoginService githubLoginService;

    public OAuth2Controller(GithubLoginService githubLoginService) {
        this.githubLoginService = githubLoginService;
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> login(@RequestBody OAuth2Request oAuth2Request) {
        return ResponseEntity.ok(githubLoginService.login(oAuth2Request));
    }
}
