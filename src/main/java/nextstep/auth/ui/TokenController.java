package nextstep.auth.ui;

import nextstep.auth.application.TokenService;
import nextstep.member.application.dto.ApplicationTokenRequest;
import nextstep.member.application.dto.ApplicationTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<ApplicationTokenResponse> createToken(@RequestBody ApplicationTokenRequest request) {
        ApplicationTokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }
}
