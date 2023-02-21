package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;
    private MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse login(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail()).stream()
                .filter(a -> a.checkPassword(tokenRequest.getPassword()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        String accessToken = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return TokenResponse.of(accessToken);
    }

    public String getEmail(String accessToken) {
        return jwtTokenProvider.getPrincipal(accessToken);
    }
}
