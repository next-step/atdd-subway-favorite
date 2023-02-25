package nextstep.auth.domain;

public class GithubAccessTokenRequest {

    private final String code;
    private final String clientId;
    private final String clientSecret;

    public GithubAccessTokenRequest(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
