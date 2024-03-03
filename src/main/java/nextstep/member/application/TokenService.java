package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.oauth.GithubClient;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.dto.GithubLoginRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider,
            GithubClient githubClient
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberRepository.findByEmailOrFail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse createTokenFromGithub(String code) {
        String accessToken = this.githubClient.requestToken(new GithubLoginRequest(code));
        System.out.println("token ? " + accessToken);
        GithubProfileResponse githubProfileResponse = this.githubClient.requestProfile(accessToken);

        String email = githubProfileResponse.getEmail();

        Member member = memberRepository
                .findByEmail(email)
                .orElse(new Member(email, null, null));
        memberRepository.save(member);

        String token = jwtTokenProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }
}
