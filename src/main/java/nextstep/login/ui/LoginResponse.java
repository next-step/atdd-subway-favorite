package nextstep.login.ui;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String accessToken;

    public LoginResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
