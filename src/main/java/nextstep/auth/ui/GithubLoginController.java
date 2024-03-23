package nextstep.auth.ui;

import nextstep.auth.application.GitHubLoginService;
import nextstep.auth.application.dto.GitHubLoginRequest;
import nextstep.auth.application.dto.GitHubLoginTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubLoginController {

    private final GitHubLoginService gitHubLoginService;

    public GithubLoginController(GitHubLoginService gitHubLoginService) {
        this.gitHubLoginService = gitHubLoginService;
    }

    @PostMapping("/login/github")
    public ResponseEntity<GitHubLoginTokenResponse> createTokenFromGithub(@RequestBody GitHubLoginRequest request) {
        GitHubLoginTokenResponse response = gitHubLoginService.createToken(request);
        return ResponseEntity.ok(response);
    }
}
