package nextstep.member.application;

import java.time.LocalDateTime;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.exception.UserNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createTokenFrom(final TokenRequest tokenRequest, final LocalDateTime localDateTime) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail()).orElseThrow(UserNotFoundException::new);

        member.validatePassword(tokenRequest.getPassword());

        return new TokenResponse(jwtTokenProvider.createToken(member, localDateTime));
    }
}
