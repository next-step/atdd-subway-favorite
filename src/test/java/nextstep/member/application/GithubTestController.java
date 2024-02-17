package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.utils.GithubResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody final GithubAccessTokenRequest tokenRequest) {
        if (tokenRequest.getCode().equals(GithubResponses.잘못된_사용자.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final GithubResponses githubResponse = GithubResponses.findByCode(tokenRequest.getCode());
        final GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubResponse.getAccessToken(), "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") final String authorization) {
        final String accessToken = authorization.split(" ")[1];
        final GithubResponses githubResponse = GithubResponses.findByAccessToken(accessToken);
        final GithubProfileResponse response = new GithubProfileResponse(githubResponse.getEmail(), githubResponse.getAge());
        return ResponseEntity.ok(response);
    }

}
