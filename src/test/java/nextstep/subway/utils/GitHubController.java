package nextstep.subway.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.dto.GitHubAccessTokenRequest;
import nextstep.member.application.dto.GitHubAccessTokenResponse;

@RestController
public class GitHubController {

    @PostMapping("/github/access-token")
    public ResponseEntity<GitHubAccessTokenResponse> getAccessToken(@RequestBody GitHubAccessTokenRequest request) {
        String accessToken = GitHubResponses.fromCode(request.getCode()).getAccessToken();
        return ResponseEntity.ok(new GitHubAccessTokenResponse(accessToken));
    }
}
