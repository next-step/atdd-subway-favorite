package nextstep.login.application.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String accessToken;

    public LoginResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
