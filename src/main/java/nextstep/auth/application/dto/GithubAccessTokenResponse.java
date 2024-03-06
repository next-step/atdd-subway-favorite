package nextstep.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubAccessTokenResponse {
    private String accessToken;

    public GithubAccessTokenResponse() {
    }

    @Builder
    public GithubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
