package nextstep.auth.application.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubAccessTokenRequest {

    private String code;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;

    public GithubAccessTokenRequest() {
    }

    public GithubAccessTokenRequest(final String code, final String clientId, final String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getCode() {
        return code;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
