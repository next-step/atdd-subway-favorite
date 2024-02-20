package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubEmailResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.jwt.JwtTokenProvider;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.MemberService;
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
