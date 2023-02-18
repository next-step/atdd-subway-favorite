package nextstep.auth.application.dto;

public class GithubAccessTokenRequest {

    private String code;
    private String clientId;
    private String clientSecret;

    private GithubAccessTokenRequest() {}

    public GithubAccessTokenRequest(final String code, final String clientId, final String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getCode() {
        return code;
    }
}
