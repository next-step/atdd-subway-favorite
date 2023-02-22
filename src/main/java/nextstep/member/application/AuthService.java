package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final GithubClient githubClient;

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        final Member member = memberService.authenticate(tokenRequest);
        return new TokenResponse(createToken(member));
    }

    public TokenResponse createToken(final GithubTokenRequest tokenRequest) {
        final String accessToken = githubClient.getAccessTokenFromGithub(tokenRequest.getCode());
        final var profile = githubClient.getGithubProfileFromGithub(accessToken);

        final Member member = memberService.findByEmailOrCreate(profile.getEmail());
        return new TokenResponse(createToken(member));
    }

    private String createToken(final Member member) {
        return jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
    }
}
