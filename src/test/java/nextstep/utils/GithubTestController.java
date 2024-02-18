package nextstep.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access-token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        System.out.println("request");
        System.out.println(request);

        GithubResponses githubResponses = GithubResponses.ofCode(request.getCode());

        return ResponseEntity.ok(new GithubAccessTokenResponse(githubResponses.accessToken()));
    }

//    @PostMapping("/github/user")
//    public ResponseEntity<GithubAccessTokenResponse> profile(@RequestBody GithubAccessTokenRequest tokenRequest) {
//
//        GithubResponses githubResponses = GithubResponses.ofCode(tokenRequest.getClientId());
//
//        return ResponseEntity.ok(new GithubAccessTokenResponse());
//    }
}
