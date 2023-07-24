package subway.auth.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.auth.token.TokenRequest;
import subway.auth.token.TokenResponse;
import subway.auth.token.TokenService;
import subway.auth.token.oauth2.github.GithubTokenRequest;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> loginWithToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok().body(response);
    }

    // for week3 - step2
//    @PostMapping("/login/github")
//    public ResponseEntity<TokenResponse> createTokenByGithub(@RequestBody GithubTokenRequest request) {
//        TokenResponse response = tokenService.createTokenFromGithub(request.getCode());
//
//        return ResponseEntity.ok(response);
//    }

        @PostMapping("/github")
    public ResponseEntity<TokenResponse> createTokenByGithub(@RequestBody GithubTokenRequest request) {
        TokenResponse response = tokenService.createTokenFromGithub(request.getCode());

        return ResponseEntity.ok(response);
    }

}
