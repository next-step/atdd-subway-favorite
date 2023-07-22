package subway.auth.token;

import lombok.Getter;

@Getter
public class TokenRequest {
    private String email;
    private String password;
}
