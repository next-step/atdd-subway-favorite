package nextstep.login.application.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private final String email;
    private final String password;

    public LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
