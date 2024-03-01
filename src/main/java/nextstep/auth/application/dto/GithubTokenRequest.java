package nextstep.auth.application.dto;

public class GithubTokenRequest {
    private String code;

    protected GithubTokenRequest() {}

    public GithubTokenRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
