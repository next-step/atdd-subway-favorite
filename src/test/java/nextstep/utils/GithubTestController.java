package nextstep.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> getGithubToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        return ResponseEntity.ok(new GithubAccessTokenResponse("access_token", "", "", ""));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubProfileResponse response = new GithubProfileResponse("email@email.com", 20);
        return ResponseEntity.ok(response);
    }
}
