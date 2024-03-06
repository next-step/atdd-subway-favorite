package nextstep.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubAccessTokenRequest {
    private String code;
    private String clientId;
    private String clientSecret;


    @Builder
    public GithubAccessTokenRequest(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
