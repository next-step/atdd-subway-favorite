package nextstep.member.application;

import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        final MemberResponse memberResponse = memberService.loginMember(request.getEmail(), request.getPassword());
        final String token = jwtTokenProvider.createToken(memberResponse.getId().toString(), memberResponse.getRoles());
        return new TokenResponse(token);
    }
}
