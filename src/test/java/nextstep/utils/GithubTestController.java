package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.auth.token.oauth2.github.GithubTokenRequest;
import nextstep.utils.dto.GithubResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubTokenRequest tokenRequest) {
        String accessToken = GithubResponses.findAccessTokenByCode(tokenRequest.getCode());
        return ResponseEntity.ok().body(new GithubAccessTokenResponse(accessToken));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String token) {
        String email = GithubResponses.findEmailByAccessToken(token);
        return ResponseEntity.ok().body(new GithubProfileResponse(email));
    }
}

