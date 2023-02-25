package nextstep.fake;

import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RequestMapping("/github-fake")
@RestController
public class GitHubFakeController {
    private static final int AUTH_TOKEN_INDEX = 1;

    @PostMapping("/access-token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubProfileResponse response = GithubResponses.find(githubResponse -> githubResponse.getCode().equals(request.getCode()));
        return ResponseEntity.ok(new GithubAccessTokenResponse(response.getAccessToken()));
    }

    @GetMapping("/profile")
    public ResponseEntity<GithubProfileResponse> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null || !authorization.startsWith("token ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorization.split(" ")[AUTH_TOKEN_INDEX];
        GithubProfileResponse response = GithubResponses.find(githubResponse -> githubResponse.getAccessToken().equals(token));
        return ResponseEntity.ok(response);
    }
}
