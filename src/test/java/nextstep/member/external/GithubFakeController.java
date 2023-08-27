package nextstep.member.external;

import lombok.RequiredArgsConstructor;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GithubFakeController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest request) {
        String fakeAccessToken = GithubFakeResponse.getAccessToken(request.getCode());

        return ResponseEntity.ok().body(GithubAccessTokenResponse.builder()
            .accessToken(fakeAccessToken)
            .build());
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split("")[1];
        String fakeEmail = GithubFakeResponse.getEmail(accessToken);

        return ResponseEntity.ok().body(GithubProfileResponse.builder()
            .email(fakeEmail)
            .build());
    }
}

