package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TokenResponse> loginWithToken(@RequestBody MemberRequest request) {
        return ResponseEntity.ok().body(authService.getToken(request.getEmail(), request.getPassword()));
    }
}
