package nextstep.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubAccessTokenRequest {

    private final String code;

    private final String clientId;

    private final String clientSecret;

    @Builder
    private GithubAccessTokenRequest(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
