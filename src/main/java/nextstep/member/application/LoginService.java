package nextstep.member.application;

import nextstep.GithubClient;
import nextstep.GithubProfileResponse;
import nextstep.member.application.dto.LoginResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoginService {

    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public LoginService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }
    
    public LoginResponse tokenLogin(String email, String password) {
        Member member = memberService.login(email, password);
        String principal = String.valueOf(member.getId());
        List<String> roles = member.getRoles();

        String token = jwtTokenProvider.createToken(principal, roles);
        return new LoginResponse(token);
    }
    
    public LoginResponse githubLogin(String githubCode) {
        String githubToken = githubClient.getAccessTokenFromGithub(githubCode);
        GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(githubToken);

        Member member = memberService.findByEmailOrCreateMember(githubProfileResponse.getEmail());
        String principal = String.valueOf(member.getId());
        List<String> roles = member.getRoles();

        String token = jwtTokenProvider.createToken(principal, roles);
        return new LoginResponse(token);
    }
}
