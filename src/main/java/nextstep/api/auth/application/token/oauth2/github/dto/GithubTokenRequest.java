package nextstep.api.auth.application.token.oauth2.github.dto;

public class GithubTokenRequest {
    private String code;

    public GithubTokenRequest() {
    }

    public GithubTokenRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
