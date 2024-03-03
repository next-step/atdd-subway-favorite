package nextstep.member.ui;

import nextstep.member.application.GithubLoginService;
import nextstep.member.application.dto.OAuth2Request;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    private final GithubLoginService githubLoginService;

    public OAuth2Controller(GithubLoginService githubLoginService) {
        this.githubLoginService = githubLoginService;
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> login(@ModelAttribute OAuth2Request oAuth2Request) {
        return ResponseEntity.ok(githubLoginService.login(oAuth2Request));
    }
}
