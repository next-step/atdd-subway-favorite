package subway.auth.token;

import lombok.Builder;

@Builder
public class TokenResponse {
    private String accessToken;
}
