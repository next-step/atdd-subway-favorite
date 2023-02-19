package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(final TokenRequest tokenRequest) {
        final Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(IllegalArgumentException::new);
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new IllegalArgumentException();
        }
        final String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
