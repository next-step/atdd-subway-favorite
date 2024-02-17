package nextstep.member.utils;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest
    ) {
        String accessToken;
        if (tokenRequest.getCode().equals(GithubMockResponses.사용자1.getCode())) {
            accessToken = GithubMockResponses.사용자1.getAccessToken();
        } else if (tokenRequest.getCode().equals(GithubMockResponses.사용자2.getCode())) {
            accessToken = GithubMockResponses.사용자2.getAccessToken();
        } else if (tokenRequest.getCode().equals(GithubMockResponses.사용자3.getCode())) {
            accessToken = GithubMockResponses.사용자3.getAccessToken();
        } else if (tokenRequest.getCode().equals(GithubMockResponses.사용자4.getCode())) {
            accessToken = GithubMockResponses.사용자4.getAccessToken();
        } else {
            accessToken = "access_token";
        }

        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization
    ) {
        String accessToken = authorization.split(" ")[1];
        String email;

        if (accessToken.equals(GithubMockResponses.사용자1.getAccessToken())) {
            email = GithubMockResponses.사용자1.getEmail();
        } else if (accessToken.equals(GithubMockResponses.사용자2.getAccessToken())) {
            email = GithubMockResponses.사용자2.getEmail();
        } else if (accessToken.equals(GithubMockResponses.사용자3.getAccessToken())) {
            email = GithubMockResponses.사용자3.getEmail();
        } else if (accessToken.equals(GithubMockResponses.사용자4.getAccessToken())) {
            email = GithubMockResponses.사용자4.getEmail();
        } else {
            email = "admin@email.com";
        }

        GithubProfileResponse response = new GithubProfileResponse(email, 20);
        return ResponseEntity.ok(response);
    }

}
