package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;

public class GithubAccessTokenResponseFixture{

    private static final String tokenType = "token_type";
    private static final String scope = "scope";
    private static final String bearer = "bearer";

    public static GithubAccessTokenResponse of(String code) {
        return new GithubAccessTokenResponse(
            GithubResponse.findByCode(code).getAccessToken(),
            tokenType,
            scope,
            bearer
        );
    }
}
