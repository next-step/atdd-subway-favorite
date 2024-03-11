package nextstep.auth.application;

import nextstep.exception.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.application.UserDetailsService;
import nextstep.member.application.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;
    private UserDetailsService userDetailService;

    public TokenService(UserDetailsService userDetailService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailService.findMemberByEmail(email);

        if (!userDetails.isEqualPassword(password)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(userDetails.getMemberEmail());

        return new TokenResponse(token);
    }

    public TokenResponse githubLogin(String code) {
        String accessToken = githubClient.requestGithubToken(code);
        GithubProfileResponse profile = githubClient.requestGithubProfile(accessToken);

        UserDetails userDetails = userDetailService.findMemberOrCreate(profile);

        String token = jwtTokenProvider.createToken(userDetails.getMemberEmail());

        return new TokenResponse(token);
    }
}
