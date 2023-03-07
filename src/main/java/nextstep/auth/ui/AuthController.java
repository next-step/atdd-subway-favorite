package nextstep.auth.ui;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.JwtTokenResponse;
import nextstep.member.application.dto.MemberRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<JwtTokenResponse> loginWithToken(@RequestBody MemberRequest request) {
        return ResponseEntity.ok().body(authService.getToken(request.getEmail(), request.getPassword()));
    }

    @PostMapping("github/access-token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubTokenRequest request) {
        return ResponseEntity.ok().body(authService.getAccessToken(request));
    }

    @GetMapping("github/profile")
    public ResponseEntity<GithubProfileResponse> getUsersProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok().body(authService.getUsersProfile(authorization));
    }

    @PostMapping("/login/github")
    public ResponseEntity<JwtTokenResponse> loginWithGithub(@RequestBody GithubTokenRequest request) {
        JwtTokenResponse jwtTokenResponse = authService.loginAndGetJwtToken(request);
        return ResponseEntity.ok().body(jwtTokenResponse);
    }
}
