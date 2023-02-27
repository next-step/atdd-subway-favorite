package nextstep.member.ui;

import nextstep.GithubClient;
import nextstep.GithubProfileResponse;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.LoginResponse;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public LoginController(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    @PostMapping("/login/token")
    public ResponseEntity<LoginResponse> login(@RequestBody TokenRequest request) {
        Member member = memberService.login(request.getEmail(), request.getPassword());
        String principal = String.valueOf(member.getId());
        
        String token = jwtTokenProvider.createToken(principal, member.getRoles());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/login/github")
    public ResponseEntity<LoginResponse> githubLogin(@RequestBody GithubLoginRequest request) {
        String code = request.getCode();

        String githubToken = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(githubToken);

        Member member = memberService.findByEmailOrCreateMember(githubProfileResponse.getEmail());

        String principal = String.valueOf(member.getId());

        String token = jwtTokenProvider.createToken(principal, member.getRoles());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
