package nextstep.api.auth.application.token.oauth2.github.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GithubAccessTokenRequest {
    private String code;
    private String client_id;
    private String client_secret;

    public GithubAccessTokenRequest(final String code, final String client_id, final String client_secret) {
        this.code = code;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }
}
