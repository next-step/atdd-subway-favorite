package nextstep.login.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class OAuthLoginRequest {

    private String code;

    public OAuthLoginRequest(final String code) {
        this.code = code;
    }
}
