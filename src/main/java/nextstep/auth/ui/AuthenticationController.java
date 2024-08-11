package nextstep.auth.ui;

import lombok.AllArgsConstructor;
import nextstep.auth.application.EmailPasswordAuthenticationService;
import nextstep.auth.infrastructure.oauth.github.GithubAuthenticationService;
import nextstep.auth.infrastructure.oauth.github.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.EmailPasswordAuthRequest;
import nextstep.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class AuthenticationController {

    private final EmailPasswordAuthenticationService emailPasswordAuthenticationService;
    private final GithubAuthenticationService githubAuthenticationService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody EmailPasswordAuthRequest request) {
        TokenResponse response = emailPasswordAuthenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> githubLogin(@RequestBody GithubAccessTokenRequest request) {
        TokenResponse response = githubAuthenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
