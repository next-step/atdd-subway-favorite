package nextstep.subway.fake;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubFakeController {

    @PostMapping("/fake/github/access-token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
        String accessToken = GithubFakeResponses.fromCode(request.getCode()).getAccessToken();
        return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken));
    }
}
