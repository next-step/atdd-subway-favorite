package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    public TokenResponse createToken(TokenRequest request){
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(RuntimeException::new);

        member.checkPassword(request.getPassword());

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }
}
