package nextstep.auth.presentation;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.TokenResponse;
import nextstep.auth.application.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> loginWithCredential(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.authenticateWithCredentials(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> loginWithGithub(@RequestBody ExternalTokenRequest externalTokenRequest) {
        TokenResponse response = tokenService.authenticateWithGithub(externalTokenRequest.getCode());
        return ResponseEntity.ok(response);
    }
}
