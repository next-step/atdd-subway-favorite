package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(AuthService authService, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        Member member = memberService.authenticate(tokenRequest.getEmail(), tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return ResponseEntity.ok().body(new TokenResponse(token));
    }
    
    @PostMapping("/github")
    public ResponseEntity<TokenResponse> loginByGithub(@RequestBody GithubTokenRequest githubTokenRequest) {
        TokenResponse token = authService.authByGithub(githubTokenRequest.getCode());
        return ResponseEntity.ok().body(token);
    }
}
