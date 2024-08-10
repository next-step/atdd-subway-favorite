package nextstep.auth.application;

import lombok.AllArgsConstructor;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public GithubAccessTokenResponse createGithubToken(String code) {

        String accessToken = githubClient.requestGithubAeccessToken(code);

        return new GithubAccessTokenResponse(accessToken);
    }
}
