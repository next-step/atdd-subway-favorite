package nextstep.member.application;

import nextstep.member.application.dto.*;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final GithubClient githubClient;

    public AuthService(JwtTokenProvider jwtTokenProvider,
                       MemberService memberService,
                       MemberRepository memberRepository,
                       GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(TokenRequest request){
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

    public GithubLoginResponse githubAuthorize(GithubLoginRequest request){
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(request.getCode());
        GithubProfileResponse profileFromGithub = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        Member member;

        try{
            member = memberRepository.findByEmail(profileFromGithub.getEmail())
                    .orElseThrow();
        } catch (Exception e) {
            member = memberRepository.save(new Member(profileFromGithub.getEmail()));
        }

        return new GithubLoginResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }
}
