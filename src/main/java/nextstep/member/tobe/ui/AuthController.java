package nextstep.member.tobe.ui;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.tobe.application.dto.TokenRequest;
import nextstep.member.tobe.application.dto.TokenResponse;
import nextstep.member.tobe.domain.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final AuthService emailPasswordAuthService;
    private final AuthService githubAuthService;

    public AuthController(
        @Qualifier("emailPasswordAuthService") AuthService emailPasswordAuthService,
        @Qualifier("githubAuthService") AuthService githubAuthService) {
        this.emailPasswordAuthService = emailPasswordAuthService;
        this.githubAuthService = githubAuthService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        TokenResponse tokenResponse = emailPasswordAuthService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> loginWithGithub(@RequestBody String code) {
        TokenRequest request = TokenRequest.ofCode(code);
        TokenResponse tokenResponse = githubAuthService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }
}

