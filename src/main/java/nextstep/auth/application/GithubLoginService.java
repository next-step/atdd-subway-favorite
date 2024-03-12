package nextstep.auth.application;

import nextstep.auth.application.dto.OAuth2Request;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.UserCreator;
import nextstep.auth.domain.UserGetter;
import nextstep.auth.domain.exception.UserException;
import nextstep.auth.ui.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GithubLoginService {

    private final OAuth2Client githubClient;
    private final UserCreator userCreator;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserGetter userGetter;

    // bean name
    public GithubLoginService(@Qualifier("githubClient") OAuth2Client githubClient,
        UserCreator userCreator, JwtTokenProvider jwtTokenProvider, UserGetter userGetter) {
        this.githubClient = githubClient;
        this.userCreator = userCreator;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userGetter = userGetter;
    }

    public TokenResponse login(OAuth2Request oAuth2Request) {
        String accessToken = githubClient.requestAccessToken(oAuth2Request.getCode());
        GithubProfileResponse githubProfile = githubClient.requestUserInfo(accessToken);
        try {
            userGetter.getUser(githubProfile.getEmail());
        } catch (UserException.NotFoundUserException e) {
            userCreator.createUser(githubProfile.getEmail(), null, githubProfile.getAge());
        }
        String token = jwtTokenProvider.createToken(githubProfile.getEmail());
        return new TokenResponse(token);
    }
}
