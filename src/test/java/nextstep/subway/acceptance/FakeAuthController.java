package nextstep.subway.acceptance;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.infrastructure.GithubProfileResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Profile("test")
@RestController
public class FakeAuthController {

    @PostMapping("/token-url")
    public ResponseEntity<TokenResponse> getAccessToken(@RequestBody GithubTokenRequest request) {
        String code = request.getCode();

        TokenResponse token = Arrays.stream(GithubResponse.values())
            .filter(githubResponse -> code.equals(githubResponse.getCode()))
            .findFirst()
            .map(GithubResponse::getAccessToken)
            .map(TokenResponse::new)
            .orElse(null);

        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/profile-url")
    public ResponseEntity<GithubProfileResponse> getGithubProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = JwtTokenProvider.parseToken(authorization);

        GithubProfileResponse response = Arrays.stream(GithubResponse.values())
            .filter(githubResponse -> token.equals(githubResponse.getAccessToken()))
            .findFirst()
            .map(githubResponse -> new GithubProfileResponse(githubResponse.getEmail()))
            .orElseThrow(IllegalArgumentException::new);

        return ResponseEntity.ok().body(response);
    }
}
