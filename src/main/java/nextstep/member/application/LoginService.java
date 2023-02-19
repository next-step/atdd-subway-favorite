package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse authorize(TokenRequest request) {
        Member findMember = memberService.findByUserEmail(request.getEmail())
                .orElseThrow(IllegalArgumentException::new);

        if (!findMember.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles());
        return TokenResponse.of(token);
    }
}