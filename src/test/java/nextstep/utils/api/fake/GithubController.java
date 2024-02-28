package nextstep.utils.api.fake;

import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
public class GithubController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<TokenResponse> accessToken(@RequestBody GithubTokenRequest tokenRequest) {
        String accessToken = "access_token";
        TokenResponse response = new TokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }
}