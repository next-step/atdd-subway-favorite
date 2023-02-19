package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.application.dto.GitHubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;

@Service
public class TokenService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GitHubClient gitHubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GitHubClient gitHubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.gitHubClient = gitHubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.authenticate(email, password);
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse createGitHubToken(String code) {
        String accessTokenFromGithub = gitHubClient.getAccessTokenFromGitHub(code);
        GitHubProfileResponse githubProfile = gitHubClient.getGithubProfileFromGithub(accessTokenFromGithub);
        Member member = memberService.findOrCreateMember(githubProfile.getEmail());
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(token);
    }
}
