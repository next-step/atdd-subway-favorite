package nextstep.member.application;

import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse login(String email, String password) {
        MemberResponse member = memberService.findMember(email, password);
        String token = tokenProvider.createToken(member.getEmail(), List.of(RoleType.ROLE_MEMBER.name()));
        return TokenResponse.of(token);
    }

}
