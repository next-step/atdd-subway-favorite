package nextstep.login.application;

import com.google.common.base.Preconditions;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import nextstep.exception.NotFoundMemberException;
import nextstep.infra.github.GithubClient;
import nextstep.login.application.dto.GithubTokenRequest;
import nextstep.login.application.dto.GithubTokenResponse;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final GithubClient githubClient;

    public TokenResponse generateToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
            .orElseThrow(NotFoundMemberException::new);

        Preconditions.checkArgument(
            Objects.equals(member.getPassword(), tokenRequest.getPassword()),
            "Password is not correct"
        );

        String accessToken = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());

        return new TokenResponse(accessToken);
    }

    public GithubTokenResponse generateGithubToken(GithubTokenRequest githubTokenRequest) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(
            githubTokenRequest.getCode()
        );

        return new GithubTokenResponse(accessTokenFromGithub);
    }
}
