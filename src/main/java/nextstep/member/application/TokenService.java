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
        // 1. 권한증서(code)로 GitHub 에서 Access Token 발급
        String accessTokenFromGithub = gitHubClient.getAccessTokenFromGitHub(code);

        // 2. Access Token 으로 GitHub 의 Resource Server 에서 사용자 프로필 조회
        GitHubProfileResponse githubProfile = gitHubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        // 3. 사용자 프로필의 이메일 주소로 회원을 조회
        // Option. 조회된 회원이 없으면 해당 이메일 주소로 회원 가입
        Member member = memberService.findOrCreateMember(githubProfile.getEmail());

        // 4. 회원 정보로 토큰 발급
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(token);
    }
}
