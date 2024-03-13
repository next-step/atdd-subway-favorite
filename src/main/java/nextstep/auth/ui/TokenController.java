package nextstep.auth.ui;

import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.application.dto.UserInfoRequest;
import nextstep.auth.application.dto.UserInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(),
            request.getPassword());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/login/oauth/access_token")
    public ResponseEntity<UserInfoResponse> getUserInfo(@ModelAttribute UserInfoRequest request) {
        UserInfoResponse response = tokenService.getUserInfo(request.getAccessToken());

        return ResponseEntity.ok(response);
    }
}
