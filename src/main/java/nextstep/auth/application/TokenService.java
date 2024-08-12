package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TokenService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        var member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        var token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse getAuthToken(final String code) {
        var tokenResponse = githubClient.getAccessTokenFromGithub(code);
        var githubProfile = githubClient.requestGithubProfile(tokenResponse.getAccessToken());

        var email = githubProfile.getEmail();
        memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email)));

        return new TokenResponse(jwtTokenProvider.createToken(email));
    }
}
