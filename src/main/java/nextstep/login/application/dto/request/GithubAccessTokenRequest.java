package nextstep.login.application.dto.request;

public class GithubAccessTokenRequest {

    private final String code;
    private final String clientId;
    private final String clientSecret;

    public GithubAccessTokenRequest(final String code, final String clientId, final String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
