package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.exception.UnauthorizedException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final GithubClient githubClient;


    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.githubClient = githubClient;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = authenticate(request.getEmail(), request.getPassword());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
    public TokenResponse loginGithub(GithubTokenRequest request) {
        String githubAccessToken = githubClient.getAccessTokenFromGithub(request.getCode());
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(githubAccessToken);

        Member member = loginOrRegister(githubProfile.getEmail());

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    String getPrincipal(String accessToken) {
        if(!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("잘못된 인증 정보입니다");
        }
        return jwtTokenProvider.getPrincipal(accessToken);
    }

    private Member authenticate(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(it -> it.checkPassword(password))
                .orElseThrow(() -> new UnauthorizedException("잘못된 인증 정보입니다"));
    }

    private Member loginOrRegister(String email) {
        Member member = memberRepository.findByEmail(email).orElse(new Member(email));
        return memberRepository.save(member);
    }
}
