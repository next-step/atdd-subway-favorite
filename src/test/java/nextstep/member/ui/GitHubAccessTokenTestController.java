package nextstep.member.ui;

import nextstep.member.application.dto.GitHubAccessTokenResponse;
import nextstep.member.application.dto.GitHubAccessTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubAccessTokenTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GitHubAccessTokenResponse> getAccessToken(@RequestBody GitHubAccessTokenRequest request) {
        GitHubAccessTokenResponse response = new GitHubAccessTokenResponse(request.getCode() + " github-access-token");
        return ResponseEntity.ok(response);
    }
}
