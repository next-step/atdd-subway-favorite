package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenDto;
import nextstep.auth.application.oauth.GithubClient;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.auth.ui.dto.TokenFromGithubRequestBody;
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

    public TokenDto createToken(String email, String password) {
        Member member = memberRepository.findByEmailOrFail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenDto(token);
    }

    @Transactional
    public TokenDto createTokenFromGithub(String code) {
        String accessToken = this.githubClient.requestToken(new TokenFromGithubRequestBody(code));
        GithubProfileResponse githubProfileResponse = this.githubClient.requestProfile(accessToken);

        String email = githubProfileResponse.getEmail();

        Member member = memberRepository
                .findByEmail(email)
                .orElse(new Member(email, null, null));
        memberRepository.save(member);

        String token = jwtTokenProvider.createToken(member.getEmail());
        return new TokenDto(token);
    }
}
