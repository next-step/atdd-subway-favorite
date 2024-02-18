package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(final MemberService memberService, final JwtTokenProvider jwtTokenProvider, final GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(final String email, final String password) {
        final Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        final String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenForGithub(final String code) {
        final String githubToken = githubClient.requestGithubToken(code);
        final GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);
        final Member member = memberService.findOrCreateMember(githubProfileResponse);
        final String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());

        return new TokenResponse(token);
    }
}
