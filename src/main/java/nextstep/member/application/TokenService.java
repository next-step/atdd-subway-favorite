package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TokenService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse createTokenWithGithubUser(String code) {
        // code로 github accessToken 발행
        GithubAccessTokenResponse accessTokenResponse = githubClient.generateAccessToken(code);

        // accessToken을 이용한 github profile 조회
        GithubProfileResponse githubProfile = githubClient.getGithubProfile(accessTokenResponse.getAccessToken());

        // 존재하는 회원이 아니면 회원가입
        Member member = memberService.findOrCreateMember(githubProfile.getEmail(), code, githubProfile.getAge());

        // 토큰발생
        return new TokenResponse(
            jwtTokenProvider.createToken(member.getEmail())
        );
    }
}
