package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
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
        Member member = memberService.findMemberByEmail(tokenRequest.getEmail());
        member.checkPassword(tokenRequest.getPassword());

        return jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());
    }

    public GithubAccessTokenResponse githubLogin(GithubLoginRequest tokenRequest) {
        return null;
    }

}
