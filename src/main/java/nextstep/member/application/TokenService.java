package nextstep.member.application;

import nextstep.member.exceptions.AuthenticationException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }


    public TokenResponse createToken(String email, int age) {
        String savedEmail = memberService.getMember(email)
                .map(Member::getEmail)
                .orElseGet(() -> {
                    MemberResponse memberResponse = memberService.createMember(new MemberRequest(email, age));
                    return memberResponse.getEmail();
                });

        String token = jwtTokenProvider.createToken(savedEmail);
        return new TokenResponse(token);
    }
}
