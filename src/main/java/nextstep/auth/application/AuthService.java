package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.GithubLoginRequest;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.infra.GithubClient;
import nextstep.member.infra.dto.GithubProfileResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse login(final TokenRequest tokenRequest) {
        final Member member = memberService.getMember(tokenRequest.getEmail());
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new IllegalArgumentException();
        }
        final String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse githubLogin(final GithubLoginRequest githubLoginRequest) {
        final String accessToken = githubClient.getAccessTokenFromGithub(githubLoginRequest.getCode());
        final GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(accessToken);
        final Member member = memberService.getMemberOrCreate(githubProfileResponse.getEmail());
        final String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
