package nextstep.test;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.ui.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses responses = GithubResponses.from(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(
            responses.getAccessToken(), "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
        @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubProfileResponse response = new GithubProfileResponse("email@email.com", 20);
        return ResponseEntity.ok(response);
    }
}

