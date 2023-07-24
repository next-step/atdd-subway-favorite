package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubResponses userInfo = GithubResponses.matchCode(request.getCode());
        return ResponseEntity.ok()
                .body(new GithubAccessTokenResponse(userInfo.getAccessToken(), "", "", ""));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("token ", "");
        GithubResponses userInfo = GithubResponses.matchAccessToken(accessToken);
        return ResponseEntity.ok()
                .body(new GithubProfileResponse(userInfo.getEmail(), 0));
    }
}

