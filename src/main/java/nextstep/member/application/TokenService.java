package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.OAuth2ProfileResponse;
import nextstep.member.application.dto.OAuth2LoginRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

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

    public TokenResponse createTokenByGithubLogin(OAuth2LoginRequest request) {
        String accessToken = githubClient.requestGithubToken(request.getCode());
        if (!StringUtils.hasText(accessToken)) {
            throw new AuthenticationException();
        }

        OAuth2ProfileResponse profileResponse = githubClient.requestGithubProfile(accessToken);
        Member member = memberService.findOrCreateMember(profileResponse);

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
