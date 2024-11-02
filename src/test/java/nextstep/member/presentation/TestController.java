package nextstep.member.presentation;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.GithubResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        var accessToken = GithubResponse.getAccessTokenByCode(tokenRequest.getCode());
        var response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        var accessToken = authorization.split(" ")[1];
        var email = GithubResponse.getEmailByAccessToken(accessToken);
        var response = GithubProfileResponse.of(email, 20);
        return ResponseEntity.ok(response);
    }

}
