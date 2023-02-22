package nextstep.member.application;

import nextstep.member.domain.Member;
import nextstep.member.infrastructure.dto.MemberInfo;
import nextstep.member.ui.request.TokenRequest;
import nextstep.member.ui.response.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberService.findMemberByEmail(request.getEmail());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return TokenResponse.of(token);
    }

    public MemberInfo findMemberByToken(String accessToken) {
        String email = jwtTokenProvider.getPrincipal(accessToken);
        return MemberInfo.from(memberService.findMemberByEmail(email));
    }
}
