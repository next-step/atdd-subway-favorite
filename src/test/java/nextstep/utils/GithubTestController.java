package nextstep.utils;

import nextstep.member.application.request.github.GithubAccessTokenRequest;
import nextstep.member.application.response.github.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String code = githubAccessTokenRequest.getCode();
        String accessToken = GithubResponses.getAccessTokenByCode(code);

        GithubAccessTokenResponse response = GithubAccessTokenResponse.from(accessToken);
        return ResponseEntity.ok(response);
    }

}
