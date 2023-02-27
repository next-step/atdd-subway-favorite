package nextstep.member.application;

import nextstep.member.application.dto.*;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberService.findMemberByEmailAndPassword(tokenRequest.getEmail(),tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse createGitHubToken(GithubTokenRequest tokenRequest) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(tokenRequest);
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        Member member = memberService.findMemberByGithubEmailOrElseCreateMember(Long.toString(githubProfile.getId()), githubProfile.getEmail());

        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(token);
    }
}
