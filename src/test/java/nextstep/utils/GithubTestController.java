package nextstep.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access-token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        System.out.println("request");
        System.out.println(request);

        GithubResponses githubResponses = GithubResponses.ofCode(request.getCode());

        return ResponseEntity.ok(new GithubAccessTokenResponse(githubResponses.accessToken()));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> profile(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses githubResponses = GithubResponses.ofToken(accessToken);

        return ResponseEntity.ok(new GithubProfileResponse(githubResponses.email(), 31));
    }
}
