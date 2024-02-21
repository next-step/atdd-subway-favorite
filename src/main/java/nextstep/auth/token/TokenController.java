package nextstep.auth.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {
    private final TokenService tokenService;
    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/login/github")
    public ResponseEntity<TokenResponse> createTokenFromGithub(@RequestParam("code") String code) {
        TokenResponse accessToken = tokenService.createTokenFromGithub(code);
        return ResponseEntity.ok(accessToken);
    }
}
