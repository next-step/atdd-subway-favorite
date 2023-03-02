package nextstep.subway.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GitHubController {

    @PostMapping("/github/access-token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
        String accessToken = GithubResponses.getGithubResponsesFromCode(request.getCode()).getAccessToken();
        return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken));
    }

    @GetMapping("/github/profile")
    public ResponseEntity<GithubProfileResponse> getProfileByAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authorization.replace("token ", "");
        String email = GithubResponses.fromAccessToken(accessToken).getEmail();
        return ResponseEntity.ok(new GithubProfileResponse(email));
    }
}
