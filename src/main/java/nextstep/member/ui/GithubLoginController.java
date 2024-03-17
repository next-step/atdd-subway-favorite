package nextstep.member.ui;

import nextstep.member.application.dto.GitHubLoginRequest;
import nextstep.member.application.dto.GitHubLoginTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubLoginController {

    @PostMapping("/login/github")
    public ResponseEntity<GitHubLoginTokenResponse> createTokenFromGithub(@RequestBody GitHubLoginRequest request) {
        return ResponseEntity.ok(null);
    }
}
