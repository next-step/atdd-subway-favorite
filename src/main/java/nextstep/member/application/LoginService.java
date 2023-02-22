package nextstep.member.application;

import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public String login(TokenRequest tokenRequest) {
        System.out.println("--_-");
        System.out.println(tokenRequest.getEmail());
        MemberResponse memberResponse = memberService.loginMember(tokenRequest.getEmail(), tokenRequest.getPassword());
        return jwtTokenProvider.createToken(memberResponse.getId().toString(), memberResponse.getRoles());
    }

}
