package nextstep.subway.fake;

import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeAuthController {
    private FakeAuthService authService;

    public FakeAuthController(FakeAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/fake/login/github")
    public ResponseEntity<TokenResponse> createGithubToken(@RequestBody GithubTokenRequest tokenRequest) {
        TokenResponse response = authService.loginGithub(tokenRequest);
        return ResponseEntity.ok().body(response);
    }
}
