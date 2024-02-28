package nextstep.core.auth.presentation;

import nextstep.core.auth.application.TokenService;
import nextstep.core.auth.application.UserDetailService;
import nextstep.core.auth.application.dto.GithubCodeRequest;
import nextstep.core.auth.application.dto.TokenRequest;
import nextstep.core.auth.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private final UserDetailService userDetailService;

    public TokenController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(userDetailService.createToken(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createTokenByGithub(@RequestBody GithubCodeRequest request) {
        return ResponseEntity.ok(userDetailService.createTokenByGithub(request.getCode()));
    }
}
