package nextstep.utils;

import nextstep.auth.AuthenticationException;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.auth.token.oauth2.github.GithubTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    private static final String AUTHORIZATION = "Authorization";
    private static final String SPACE = " ";
    private static final int TOKEN_VALUE_INDEX = 1;

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubTokenRequest request) {
        String accessToken = GithubResponses.findAccessTokenByCode(request.getCode());
        return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader(AUTHORIZATION) String token) {
        String[] tokenArray = token.split(SPACE);
        String emailByAccessToken = GithubResponses.findEmailByAccessToken(tokenArray[TOKEN_VALUE_INDEX]);
        return ResponseEntity.ok(new GithubProfileResponse(emailByAccessToken));
    }
}

