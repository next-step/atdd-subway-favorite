package nextstep.member.ui;

import nextstep.member.application.dto.GitHubAccessTokenResponse;
import nextstep.member.application.dto.GitHubAccessTokenRequest;
import nextstep.member.application.dto.GitHubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubAccessTokenTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GitHubAccessTokenResponse> getAccessToken(@RequestBody GitHubAccessTokenRequest request) {
        GitHubAccessTokenResponse response = new GitHubAccessTokenResponse(request.getCode() + " github-access-token");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/github/user")
    public ResponseEntity<GitHubProfileResponse> getUserProfile(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        String email = accessToken.replace("_access_token", "@gmail.com");
        int age = 20;

        GitHubProfileResponse response = new GitHubProfileResponse(email, age);

        return ResponseEntity.ok(response);
    }
}
