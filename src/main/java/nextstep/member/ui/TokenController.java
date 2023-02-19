package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        Member member = memberService.findMember(request.getEmail(), request.getPassword());

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());

        return ResponseEntity.ok(new TokenResponse(token));
    }
}
