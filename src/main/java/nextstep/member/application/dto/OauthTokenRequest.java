package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OauthTokenRequest {

    private String code;

    public OauthTokenRequest(String code) {
        this.code = code;
    }
}
