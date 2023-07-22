package nextstep.api.auth.application.token.oauth2.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GithubAccessTokenResponse {
    private String accessToken;
    private String tokenType;
    private String scope;
    private String bearer;

    public GithubAccessTokenResponse(final String accessToken,
                                     final String tokenType,
                                     final String scope,
                                     final String bearer) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.bearer = bearer;
    }
}
