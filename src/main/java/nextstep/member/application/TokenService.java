package nextstep.member.application;

import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.common.ErrorResponse;
import nextstep.member.common.LoginException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;


@Service
public class TokenService {

    private final JwtTokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    public TokenService(JwtTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse login(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();

        Member member = memberRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new LoginException(ErrorResponse.INVALIDATION_LOGIN_INFORMATION));
        String token = tokenProvider.createToken(email, member.getRoles());

        return new TokenResponse(token);
    }

    public MemberResponse getMember(String token) {
        String email = tokenProvider.getPrincipal(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new LoginException(ErrorResponse.NOT_FOUND_EMAIL));
        return MemberResponse.of(member);
    }
}
