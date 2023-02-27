package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.GithubLoginResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {this.authService = authService;}

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(
            @RequestBody TokenRequest request){

        return ResponseEntity.ok().body(authService.createToken(request));
    }

    @PostMapping("/login/github")
    public ResponseEntity<GithubLoginResponse> githubLogin(@RequestBody GithubLoginRequest request){

        GithubLoginResponse response = authService.githubAuthorize(request);

        return ResponseEntity.ok().body(response);
    }
}
