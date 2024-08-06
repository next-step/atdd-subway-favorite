package nextstep.utils;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeGithubController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = "accessToken";
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/github/user")
//    public ResponseEntity<GithubProfileResponse> user(
//            @RequestHeader("Authorization") String authorization) {
//        String accessToken = authorization.split(" ")[1];
//        GithubProfileResponse response = new GithubProfileResponse("email@email.com", 20);
//        return ResponseEntity.ok(response);
//    }
}

