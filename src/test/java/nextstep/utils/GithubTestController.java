package nextstep.utils;

import java.util.Objects;
import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody
        GithubAccessTokenRequest request) {

        for(GithubResponses resposne : GithubResponses.values()) {
            if(resposne.getCode().equals(request.getCode())) {
                return ResponseEntity.ok(new GithubAccessTokenResponse(resposne.getAccessToken(), "tokenType", "scope", ""));
            }
        }

        throw new IllegalArgumentException("잘못된 코드입니다.");
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> profile(@RequestHeader("Authorization") String bearerToken) {
        final String accessToken = Objects.requireNonNull(bearerToken).replace("bearer ", "");

        for(GithubResponses response : GithubResponses.values()) {
            if(response.getAccessToken().equals(accessToken)) {
                GithubProfileResponse profile = new GithubProfileResponse(response.getEmail(), 10);

                return ResponseEntity.ok().body(profile);
            }
        }

        throw new IllegalArgumentException("잘못된 코드입니다.");
    }
}
