package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.common.retrofit.RetrofitExecutor;
import nextstep.member.application.dto.*;
import nextstep.member.application.exception.GithubOauthConnectionException;
import nextstep.member.application.exception.MemberErrorCode;
import nextstep.member.config.GithubOauthProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubOauthAdapterImpl implements GithubOauthAdapter {

    private final GithubClient githubClient;

    private final GithubOauthProperty githubOauthProperty;

    @Override
    public GithubResponse login(String code) {
        GithubOauthAccessTokenResponse githubOauthAccessTokenResponse = callAccessTokenApi(code);
        GithubOauthProfileResponse githubOauthProfileResponse = callProfileApi(githubOauthAccessTokenResponse);

        return GithubResponseImpl.builder()
                .code(code)
                .accessToken(githubOauthAccessTokenResponse.getAccessToken())
                .email(githubOauthProfileResponse.getEmail())
                .build();
    }

    private GithubOauthAccessTokenResponse callAccessTokenApi(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = GithubAccessTokenRequest.builder()
                .clientId(githubOauthProperty.getClientId())
                .clientSecret(githubOauthProperty.getClientSecret())
                .code(code)
                .build();

        return RetrofitExecutor.execute(
                githubClient.callAccessTokenApi(
                        githubOauthProperty.getTokenUrl(),
                        githubAccessTokenRequest
                ),
                () -> new GithubOauthConnectionException(MemberErrorCode.CONNECTION_FAIL_GITHUB)
        );
    }

    private GithubOauthProfileResponse callProfileApi(GithubOauthAccessTokenResponse githubOauthAccessTokenResponse) {
        return RetrofitExecutor.execute(
                githubClient.callProfileApi(
                        githubOauthProperty.getProfileUrl(),
                        githubOauthAccessTokenResponse.getAccessToken()
                ),
                () -> new GithubOauthConnectionException(MemberErrorCode.CONNECTION_FAIL_GITHUB)
        );
    }
}
