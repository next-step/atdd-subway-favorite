package nextstep.member.presentation;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.LoginRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
public class TokenController {

    private TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> createGithubToken(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = tokenService.createGithubAccessToken(loginRequest.getCode());

        return ResponseEntity.ok(tokenService.requestProfile(tokenResponse.getAccessToken()));
    }
}
