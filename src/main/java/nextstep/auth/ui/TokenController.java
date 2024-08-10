package nextstep.auth.ui;

import lombok.AllArgsConstructor;
import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TokenController {
    private TokenService tokenService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<GithubAccessTokenResponse> createGithubToken(@RequestBody GithubAccessTokenRequest request) {
        GithubAccessTokenResponse response = tokenService.createGithubToken(request.getCode());

        return ResponseEntity.ok(response);
    }
}
