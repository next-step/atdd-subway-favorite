package nextstep.member.application.dto.github;

public class GithubAccessTokenRequest {

    private String code;
    private String clientId;
    private String clientSecret;

    private GithubAccessTokenRequest() {
    }

    public GithubAccessTokenRequest(String code, String clientId, String clientSecret) {
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
