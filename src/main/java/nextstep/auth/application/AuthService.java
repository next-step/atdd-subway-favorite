package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubMemberRequest;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public AuthService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse authenticateWithEmail(String email, String password) {
        Member member = userDetailService.findMemberByEmailOrElseThrow(email);
        if (!member.match(password)) {
            throw new AuthenticationException();
        }
        return createTokenAndResponse(member.getEmail());
    }

    public TokenResponse authenticateWithGithub(String code) {
        GithubProfileResponse response = githubClient.requestGithubProfile(code);
        Optional<Member> member = userDetailService.findMemberByEmail(response.getEmail());
        if (member.isEmpty()) {
            userDetailService.createGithubMember(new GithubMemberRequest(response.getEmail(), response.getAge()));
            return createTokenAndResponse(response.getEmail());
        }
        return createTokenAndResponse(member.get().getEmail());
    }

    private TokenResponse createTokenAndResponse(String email) {
        String accessToken = jwtTokenProvider.createToken(email);
        return new TokenResponse(accessToken);
    }
}
