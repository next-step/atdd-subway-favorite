package nextstep.member.application;

import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.NotFoundMemberException;
import nextstep.member.infrastructure.GithubClient;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final GithubClient githubClient;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberService memberService, GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.githubClient = githubClient;
    }

    public String login(TokenRequest tokenRequest) {
        Member member = memberService.findMemberByEmail(tokenRequest.getEmail());
        member.checkPassword(tokenRequest.getPassword());

        return jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());
    }

    public String githubLogin(GithubLoginRequest tokenRequest) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(tokenRequest.getCode());
        GithubProfileResponse profileFromGithub = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        Member member = getMember(profileFromGithub);

        return jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());
    }

    private Member getMember(GithubProfileResponse profileFromGithub) {
        try {
            return memberService.findMemberByEmail(profileFromGithub.getEmail());
        } catch (NotFoundMemberException e) {
            return memberService.createMember(new MemberRequest(profileFromGithub.getEmail()));
        }
    }

}
