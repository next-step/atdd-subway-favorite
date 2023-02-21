package nextstep.github.application.dto;

public class GithubAccessTokenRequest {
    private String code;
    private String client_id;
    private String client_secret;

    private GithubAccessTokenRequest() {
    }

    public GithubAccessTokenRequest(String code, String client_id, String client_secret) {
        this.code = code;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }
}
