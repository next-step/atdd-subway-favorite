package nextstep.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubAccessTokenRequest {

    private String code;
    private String clientId;
    private String clientSecret;

    @Builder
    private GithubAccessTokenRequest(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public static GithubAccessTokenRequest of(String code, String clientId, String clientSecret) {
        return GithubAccessTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
