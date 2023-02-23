package nextstep.member.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubAccessTokenRequest {

    private String code;
    private String clientId;
    private String clientSecret;

    public GithubAccessTokenRequest(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
