package nextstep.member.application;

import nextstep.exception.MemberInvalidException;
import nextstep.exception.MemberNotFoundException;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(final TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        if (checkInvalidMember(tokenRequest, member)) {
            throw new MemberInvalidException();
        }
        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

    private static boolean checkInvalidMember(TokenRequest tokenRequest, Member member) {
        return !member.checkPassword(tokenRequest.getPassword());
    }
}
