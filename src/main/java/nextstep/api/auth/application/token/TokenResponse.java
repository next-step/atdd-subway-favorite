package nextstep.api.auth.application.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenResponse {
    private String accessToken;

    public TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
