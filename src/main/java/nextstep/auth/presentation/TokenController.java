package nextstep.auth.presentation;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.TokenResponse;
import nextstep.auth.application.TokenService;
import nextstep.auth.infrastructure.GithubAccessTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        TokenResponse response = tokenService.getAuthToken(githubAccessTokenRequest.getCode());
        return ResponseEntity.ok(response);
    }
}
