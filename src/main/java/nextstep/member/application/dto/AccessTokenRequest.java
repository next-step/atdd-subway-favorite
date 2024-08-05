package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessTokenRequest {

    private String code;

    public AccessTokenRequest(String code) {
        this.code = code;
    }
}
