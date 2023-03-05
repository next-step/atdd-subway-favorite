package nextstep.auth.application;

import nextstep.common.exception.AuthenticationException;
import nextstep.common.config.JwtTokenProvider;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final GithubClient githubClient;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.githubClient = githubClient;
    }

    public TokenResponse login(TokenRequest tokenRequest) {
        final Member member = memberRepository.findByEmail(tokenRequest.getEmail()).orElseThrow(AuthenticationException::new);

        member.validatePassword(tokenRequest.getPassword());

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse loginGithub(String code) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
        String githubEmail = githubClient.getGithubProfileFromGithub(accessTokenFromGithub).getEmail();

        Member member = findMemberOrCreateMember(githubEmail);

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    private Member findMemberOrCreateMember(String githubEmail) {
        return memberRepository.findByEmail(githubEmail)
                .orElseGet(() -> memberRepository.save(new Member(githubEmail)));
    }
}
