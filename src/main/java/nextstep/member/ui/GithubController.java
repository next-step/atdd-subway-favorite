package nextstep.member.ui;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubProfileResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
@Profile("!test")
public class GithubController {

    private final TokenService tokenService;

    public GithubController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        TokenResponse tokenResponse = tokenService.login(githubAccessTokenRequest.getCode());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/member/me")
    public ResponseEntity<GithubProfileResponse> getMember(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {

        GithubProfileResponse gitHubMember = tokenService.getGitHubMember(accessToken);
        return ResponseEntity.ok().body(gitHubMember);
    }
}
