package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.config.AuthToken;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<GithubAccessTokenResponse> createGithubToken(@RequestBody GithubLoginRequest request) {
        GithubAccessTokenResponse response = authService.loginByGithub(request.getCode());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/github")
    public ResponseEntity<GithubProfileResponse> getUserProfileFromGithub(@AuthToken String accessToken) {
        GithubProfileResponse response = authService.getUserInfoFromGithub(accessToken);
        return ResponseEntity.ok().body(response);
    }


}
