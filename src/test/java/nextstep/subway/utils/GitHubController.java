package nextstep.subway.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubController {

    @PostMapping("/access-token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
        String accessToken = GithubResponses.getGithubResponsesFromCode(request.getCode()).getAccessToken();
        return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken));
    }
}
