package nextstep.member.application.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private final String accessToken;

    public TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
