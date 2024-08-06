package nextstep.auth.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.controller.dto.OAuthCodeRequest;
import nextstep.auth.controller.dto.TokenRequest;
import nextstep.auth.controller.dto.TokenResponse;
import nextstep.auth.domain.command.AuthCommander;
import nextstep.auth.domain.entity.SocialOAuthProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final AuthCommander authCommander;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        String token = authCommander.authenticateEmailPassword(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createToken(@RequestBody OAuthCodeRequest request) {
        String token = authCommander.authenticateSocialOAuth(request.toCommand(SocialOAuthProvider.GITHUB));
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
