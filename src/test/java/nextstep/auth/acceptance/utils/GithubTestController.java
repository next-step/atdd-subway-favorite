package nextstep.auth.acceptance.utils;

import nextstep.auth.oauth2.github.dto.GithubAccessTokenRequest;
import nextstep.auth.oauth2.github.dto.GithubAccessTokenResponse;
import nextstep.auth.oauth2.github.dto.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubMockUser user = GithubMockUser.getUserByCode(request.getCode());
        GithubAccessTokenResponse response = user.toGithubAccessTokenResponse();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        GithubMockUser user = GithubMockUser.getUserByAccessToken(accessToken);
        GithubProfileResponse response = user.toGithubProfileResponse();

        return ResponseEntity.ok(response);
    }
}

