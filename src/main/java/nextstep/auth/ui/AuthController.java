package nextstep.auth.ui;

import nextstep.auth.application.GithubLoginService;
import nextstep.auth.application.dto.GitHubLoginRequest;
import nextstep.auth.application.dto.GithubLoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final GithubLoginService githubLoginService;

    public AuthController(GithubLoginService githubLoginService) {
        this.githubLoginService = githubLoginService;
    }

    @PostMapping("/login/github")
    public ResponseEntity<GithubLoginResponse> login(
            @RequestBody GitHubLoginRequest request
    ) {
         return ResponseEntity.ok(githubLoginService.login(request));
    }
}
