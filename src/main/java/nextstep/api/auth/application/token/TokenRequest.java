package nextstep.api.auth.application.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
