package nextstep.login.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class GithubAccessTokenResponse {

    private String accessToken;

    public GithubAccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
