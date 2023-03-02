package nextstep.login.application.dto.request;

import lombok.Getter;

@Getter
public class TokenLoginRequest {

    private final String email;
    private final String password;

    public TokenLoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
