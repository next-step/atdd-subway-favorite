package nextstep.authentication.ui;

import nextstep.authentication.application.TokenService;
import nextstep.authentication.application.dto.GithubCodeRequest;
import nextstep.authentication.application.dto.TokenRequest;
import nextstep.authentication.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private TokenService tokenService;

    public AuthenticationController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createToken(@RequestBody GithubCodeRequest githubCodeRequest) {
        TokenResponse response = tokenService.createToken(githubCodeRequest.getCode());
        return ResponseEntity.ok(response);
    }
}
