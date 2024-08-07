package nextstep.member.application;

import lombok.AllArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.TokenResponse;
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
