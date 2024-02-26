package nextstep.member.ui;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.ui.dto.GithubProfileResponse;
import nextstep.member.ui.dto.OAuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private TokenService tokenService;
    private GithubClient githubClient;

    public TokenController(TokenService tokenService, GithubClient githubClient) {
        this.tokenService = tokenService;
        this.githubClient = githubClient;
    }


    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createToke(@RequestBody OAuthRequest request) {
        String token = githubClient.getAccessTokenFromGithub(request.getCode());
        GithubProfileResponse userProfile = githubClient.getUserProfile(token);
        TokenResponse response = tokenService.createToken(userProfile.getEmail(), userProfile.getAge());
        return ResponseEntity.ok(response);
    }
}
