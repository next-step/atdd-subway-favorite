package nextstep.authentication.application;

import nextstep.authentication.domain.AuthenticationInformation;
import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.authentication.application.dto.TokenResponse;
import nextstep.authentication.domain.LoginMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TokenService {

    private AuthenticationService authenticationService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(AuthenticationService authenticationService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.authenticationService = authenticationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        AuthenticationInformation authenticationInformation = authenticationService.findMemberByEmail(email);
        authenticationInformation.verification(password);

        String token = jwtTokenProvider.createToken(authenticationInformation.getEmail(), authenticationInformation.getId());

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse createToken(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);

        LoginMember loginMember = authenticationService.lookUpOrCreateMember(githubProfileResponse);

        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getId());

        return new TokenResponse(token);
    }
}
