package nextstep.auth.application.dto;

public class GitHubAccessTokenRequest {

    private String code;
    private String clientId;
    private String clientSecret;

    public GitHubAccessTokenRequest() {
    }

    public GitHubAccessTokenRequest(String code, String clientId, String clientSecret) {
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
