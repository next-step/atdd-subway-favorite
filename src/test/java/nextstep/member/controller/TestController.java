package nextstep.member.controller;

import java.util.Optional;
import nextstep.member.domain.GithubResponses;
import nextstep.member.domain.dto.GithubAccessTokenRequest;
import nextstep.member.domain.dto.GithubAccessTokenResponse;
import nextstep.member.domain.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/github")
@RestController
public class TestController {

    @PostMapping("/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses githubResponses = GithubResponses.fromCode(tokenRequest.getCode())
                .orElseThrow(IllegalArgumentException::new);
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubResponses.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses githubResponses = GithubResponses.fromAccessCode(accessToken)
                .orElseThrow(IllegalArgumentException::new);
        GithubProfileResponse response = new GithubProfileResponse(githubResponses.getEmail());
        return ResponseEntity.ok(response);
    }
}
