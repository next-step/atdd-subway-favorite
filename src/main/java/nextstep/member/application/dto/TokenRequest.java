package nextstep.member.application.dto;

import lombok.Getter;

@Getter
public class TokenRequest {
    private final String email;
    private final String password;

    public TokenRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
