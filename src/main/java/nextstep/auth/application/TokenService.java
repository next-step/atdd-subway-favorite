package nextstep.auth.application;

import nextstep.favorite.application.GithubClient;
import nextstep.favorite.application.dto.GithubProfileResponse;
import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(final String code) {
        String githubToken = githubClient.requestGithubToken(code);
        if (StringUtils.isEmpty(githubToken)) {
            throw new AuthenticationException();
        }
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);
        Member member = memberService.findOrCreateMember(githubProfileResponse);
        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
