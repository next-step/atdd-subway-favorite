package nextstep.auth.application.dto;

public class GithubTokenRequest {
    private String code;
    private String clientId;
    private String clientSecret;

    protected GithubTokenRequest() {
    }

    public GithubTokenRequest(String code, String clientId, String clientSecret) {
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
