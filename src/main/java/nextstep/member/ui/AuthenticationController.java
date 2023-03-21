package nextstep.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.service.AuthenticationService;
import nextstep.member.application.dto.GithubLoginRequest;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        TokenResponse response = authenticationService.basicLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<GithubAccessTokenResponse> githubLogin(@RequestBody GithubLoginRequest request) {
        GithubAccessTokenResponse githubAccessTokenResponse = authenticationService.githubLogin(request.getCode());
        return ResponseEntity.ok(githubAccessTokenResponse);
    }
}
