package nextstep.member.controller;

import lombok.RequiredArgsConstructor;
import nextstep.member.controller.dto.OAuthCodeRequest;
import nextstep.member.controller.dto.TokenRequest;
import nextstep.member.controller.dto.TokenResponse;
import nextstep.member.domain.command.TokenCommander;
import nextstep.member.domain.command.authenticator.SocialOAuthProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenCommander tokenCommander;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        String token = tokenCommander.createToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createToken(@RequestBody OAuthCodeRequest request) {
        String token = tokenCommander.authenticateSocialOAuth(request.toCommand(SocialOAuthProvider.GITHUB));
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
