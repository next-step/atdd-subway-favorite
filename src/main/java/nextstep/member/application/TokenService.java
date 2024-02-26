package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.application.GithubClient;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email)
            .orElseThrow(() -> new AuthenticationException("인증 정보가 올바르지 않습니다."));

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException("인증 정보가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromGithub(final String code) {
        final var accessToken = githubClient.requestGithubToken(code);
        final var profile = githubClient.requestGithubProfile(accessToken);

        var member = memberService.findMemberByEmail(profile.getEmail())
            .orElseGet(() -> memberService.createMember(
                profile.getEmail(),
                // TODO password 처리
                "password",
                profile.getAge()
            ));

        return createToken(member.getEmail(), member.getPassword());
    }
}
