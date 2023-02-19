package nextstep.subway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.dto.GitHubAccessTokenRequest;
import nextstep.member.application.dto.GitHubAccessTokenResponse;
import nextstep.member.application.dto.GitHubProfileResponse;

@RestController
public class GitHubController {

    @PostMapping("/github/access-token")
    public ResponseEntity<GitHubAccessTokenResponse> getAccessToken(@RequestBody GitHubAccessTokenRequest request) {
        String accessToken = GitHubResponses.fromCode(request.getCode()).getAccessToken();
        return ResponseEntity.ok(new GitHubAccessTokenResponse(accessToken));
    }

    @GetMapping("/github/profile")
    public ResponseEntity<GitHubProfileResponse> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authorization.split(" ")[1];
        String email = GitHubResponses.fromAccessToken(accessToken).getEmail();
        return ResponseEntity.ok(new GitHubProfileResponse(email));
    }
}
