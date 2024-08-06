package nextstep.member.fixture;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthFixture {
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    private static final String PASSWORD = "password";

    public AuthFixture(final MemberRepository memberRepository, final TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    public String getMemberAccessToken(final String email) {
        memberRepository.save(new Member(email, PASSWORD, 29));
        TokenResponse response = tokenService.createToken(email, PASSWORD);
        return response.getAccessToken();
    }
}
