package nextstep.member.application;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.common.ErrorResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LoginService {

    private final JwtTokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    public LoginService(JwtTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse login(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();

        Member member = memberRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new IllegalArgumentException(ErrorResponse.INVALIDATION_LOGIN_INFORMATION.getMessage()));
        String token = tokenProvider.createToken(email, member.getRoles());

        return new TokenResponse(token);
    }
}
