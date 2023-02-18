package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.auth.GithubClient;
import nextstep.member.auth.JwtTokenProvider;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

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

    public String createToken(String email, String password) {
        Member member = memberService.verificateAndFindMember(email, password);
        return jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
    }

    public String createGithubToken(String code){
        System.out.println("code = " + code);
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse response = githubClient.getGithubProfileFromGithub(accessToken);
        memberService.findOrCreateMember(response.getEmail());

        return accessToken;
    }

}
