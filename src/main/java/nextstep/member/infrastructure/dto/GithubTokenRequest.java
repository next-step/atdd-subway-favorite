package nextstep.member.infrastructure.dto;

public class GithubTokenRequest {
    private final String code;
    private final String clientId;
    private final String clientSecret;

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
