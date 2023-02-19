package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = authenticate(request.getEmail(), request.getPassword());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
    String getPrincipal(String accessToken) {
        if(!jwtTokenProvider.validateToken(accessToken)) {
            // TODO: throw shit;
        }
        return jwtTokenProvider.getPrincipal(accessToken);
    }

    private Member authenticate(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(it -> it.checkPassword(password))
                .orElseThrow(RuntimeException::new);
    }
}
