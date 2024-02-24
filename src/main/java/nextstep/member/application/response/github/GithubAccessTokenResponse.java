package nextstep.member.application.response.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private GithubAccessTokenResponse() {
    }

    private GithubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static GithubAccessTokenResponse from(String accessToken) {
        return new GithubAccessTokenResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

}
