package nextstep.member.application.dto;

public class GithubAccessTokenRequest {
    private String clientId;
    private String clientSecret;
    private String code;
    private String redirectUri;
    private String state;

    public GithubAccessTokenRequest(
        String clientId,
        String clientSecret,
        String code,
        String redirectUri,
        String state
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.state = state;
    }

    public static GithubAccessTokenRequest of(
        String clientId,
        String clientSecret,
        String code,
        String redirectUri,
        String state) {
        return new GithubAccessTokenRequest(clientId, clientSecret, code, redirectUri, state);
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCode() {
        return code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getState() {
        return state;
    }
}

