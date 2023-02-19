package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    AuthService(MemberService memberService, JwtTokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberService.validateMemberAndReturn(request);
        String token = tokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

}
