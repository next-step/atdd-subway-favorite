package nextstep.member.ui;

import nextstep.member.auth.GithubClient;
import nextstep.member.application.LoginService;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest tokenRequest){
        String token = loginService.createToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        return ResponseEntity.ok().body(new TokenResponse(token));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createGithubToken(@RequestBody GithubAccessTokenRequest tokenRequest){
        String token = loginService.createGithubToken(tokenRequest.getCode());
        return ResponseEntity.ok().body(new TokenResponse(token));
    }

}
