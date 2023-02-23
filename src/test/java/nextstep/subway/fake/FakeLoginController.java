package nextstep.subway.fake;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;

@RestController
public class FakeLoginController {
    private final FakeLoginService loginService;

    public FakeLoginController(FakeLoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/fake/login/github")
    public ResponseEntity<TokenResponse> createGithubToken(@RequestBody GithubTokenRequest githubTokenRequest) {
        String accessToken = loginService.createGithubToken(githubTokenRequest.getCode());
        return ResponseEntity.ok().body(new TokenResponse(accessToken));
    }
}
