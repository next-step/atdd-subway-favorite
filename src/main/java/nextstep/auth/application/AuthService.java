package nextstep.auth.application;

import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.common.constants.ErrorConstant.INVALID_EMAIL_PASSWORD;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(final TokenRequest tokenRequest) {
        final Member member = memberService.findByEmail(tokenRequest.getEmail());
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new IllegalArgumentException(INVALID_EMAIL_PASSWORD);
        }

        return createToken(member);
    }

    private TokenResponse createToken(final Member member) {
        final String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
