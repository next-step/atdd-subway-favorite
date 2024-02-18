package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubEmailResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRegister;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final MemberService memberService;
    private final MemberRegister memberRegister;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenGithub(String code) {
        GithubAccessTokenResponse tokenResponse = githubClient.requestAccessToken(code);
        GithubEmailResponse emailResponse = githubClient.requestGithubEmail(tokenResponse.getAccessToken());

        Member member = memberRegister.findOrCreate(emailResponse.getEmail());
        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
