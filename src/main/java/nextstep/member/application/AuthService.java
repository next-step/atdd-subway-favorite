package nextstep.member.application;

import java.time.LocalDateTime;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public AuthService(
            final MemberRepository memberRepository,
            final JwtTokenProvider jwtTokenProvider,
            final GithubClient githubClient
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createTokenFrom(final TokenRequest tokenRequest, final LocalDateTime localDateTime) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail()).orElseThrow(MemberNotFoundException::new);

        member.validatePassword(tokenRequest.getPassword());

        return new TokenResponse(jwtTokenProvider.createToken(member, localDateTime));
    }

    public TokenResponse createTokenFrom(final GithubLoginRequest githubLoginRequest) {
        String accessToken = githubClient.getAccessTokenFromGithub(githubLoginRequest.getCode());
        return new TokenResponse(accessToken);
    }
}
