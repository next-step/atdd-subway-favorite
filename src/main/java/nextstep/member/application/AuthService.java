package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse loginMember(TokenRequest tokenRequest) {
        Member member = memberService.findMemberByEmailAndPassword(tokenRequest.getEmail(),tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
