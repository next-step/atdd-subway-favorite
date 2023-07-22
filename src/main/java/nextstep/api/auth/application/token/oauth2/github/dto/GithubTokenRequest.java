package nextstep.api.auth.application.token.oauth2.github.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GithubTokenRequest {
    private String code;

    public GithubTokenRequest(final String code) {
        this.code = code;
    }
}
