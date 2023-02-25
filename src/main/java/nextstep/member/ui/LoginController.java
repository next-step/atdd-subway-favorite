package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.LoginService;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(loginService.createToken(tokenRequest));
    }

    @PostMapping("/login/github")
    public ResponseEntity<GithubAccessTokenResponse> getGithubToken(@RequestBody String code) {
        return ResponseEntity.ok(loginService.getGithubToken(code));
    }

    @PostMapping("/login/authorization")
    public ResponseEntity<GithubAccessTokenResponse> getAuth(
            @RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok(loginService.getAuth(request));
    }
}
