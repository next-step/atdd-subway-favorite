package nextstep.auth.application.github;

import nextstep.auth.application.dto.OAuth2Response;
import nextstep.utils.GithubResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody final GithubAccessTokenRequest tokenRequest) {
        final GithubResponses githubResponse = GithubResponses.findByCode(tokenRequest.getCode());
        if (githubResponse == GithubResponses.잘못된_사용자) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubResponse.getAccessToken(), "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<OAuth2Response> user(
            @RequestHeader("Authorization") final String authorization) {
        final String accessToken = authorization.split(" ")[1];
        final GithubResponses githubResponse = GithubResponses.findByAccessToken(accessToken);
        final OAuth2Response response = new OAuth2Response(githubResponse.getEmail(), githubResponse.getAge());
        return ResponseEntity.ok(response);
    }

}
