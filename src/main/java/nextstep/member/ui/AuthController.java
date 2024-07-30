package nextstep.member.ui;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.TokenService;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> loginWithGithub(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        TokenResponse tokenResponse = tokenService.createTokenFromGithub(code);
        return ResponseEntity.ok(tokenResponse);
    }
}