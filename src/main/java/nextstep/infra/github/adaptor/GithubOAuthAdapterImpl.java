package nextstep.infra.github.adaptor;

import lombok.RequiredArgsConstructor;
import nextstep.config.github.GithubOAuthProperty;
import nextstep.exception.GithubConnectionException;
import nextstep.infra.github.GithubClient;
import nextstep.infra.github.dto.GithubAccessTokenRequest;
import nextstep.infra.github.dto.GithubAccessTokenResponse;
import nextstep.infra.github.dto.GithubProfileResponse;
import nextstep.retrofit.RetrofitExecutor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class GithubOAuthAdapterImpl implements GithubOAuthAdapter {
    public static final String TOKEN_PREFIX = "token ";

    private final GithubClient githubClient;

    private final GithubOAuthProperty githubOAuthProperty;


    @Override
    public GithubProfileResponse login(String code) {
        GithubAccessTokenResponse githubAccessTokenResponse = callAccessTokenApi(code);

        String accessToken = githubAccessTokenResponse.getAccessToken();

        return callProfileApi(accessToken);
    }

    private GithubAccessTokenResponse callAccessTokenApi(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = GithubAccessTokenRequest.builder()
            .clientId(githubOAuthProperty.getClientId())
            .clientSecret(githubOAuthProperty.getClientSecret())
            .code(code)
            .build();

        return RetrofitExecutor.execute(
            githubClient.callAccessTokenApi(
                githubOAuthProperty.getTokenUrl(),
                githubAccessTokenRequest
            ),
            GithubConnectionException::new
        );
    }

    private GithubProfileResponse callProfileApi(String accessToken) {
        return RetrofitExecutor.execute(
            githubClient.callGithubProfileApi(
                githubOAuthProperty.getProfileUrl(),
                TOKEN_PREFIX + accessToken
            ),
            GithubConnectionException::new
        );
    }
}
