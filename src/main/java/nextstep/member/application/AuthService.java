package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse createToken(TokenRequest request) {
        Member member = memberService.findMemberByEmail(request.getEmail());
        if (!member.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException();
        }
        String token = tokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
