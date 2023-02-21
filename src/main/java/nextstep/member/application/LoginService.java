package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Email 입니다."));
        if (!member.getPassword().equals(tokenRequest.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

}
